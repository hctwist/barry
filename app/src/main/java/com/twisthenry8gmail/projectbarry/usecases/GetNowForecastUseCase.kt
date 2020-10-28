package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.*
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVSource
import com.twisthenry8gmail.projectbarry.data.openuv.RealTimeUVRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetNowForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository,
    private val realTimeUVRepository: RealTimeUVRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(location: LocationData): Result<CurrentForecast> {

        return coroutineScope {

            val oneCallDataJob = async { oneCallRepository.get(location) }
            val realTimeUVDataJob = async { realTimeUVRepository.get(location) }

            val oneCallData = oneCallDataJob.await()
            val realTimeUVData = realTimeUVDataJob.await()

            if (oneCallData is Result.Success && realTimeUVData is Result.Success) {

                success(buildCurrentForecast(oneCallData.data, realTimeUVData.data))
            } else {

                failure()
            }
        }
    }

    private fun buildCurrentForecast(
        oneCall: OpenWeatherSource.OneCallData,
        uv: OpenUVSource.RealTimeData
    ): CurrentForecast {

        val temperatureScale = settingsRepository.getTemperatureScale()

        val today = oneCall.daily[0]

        val nHours = 2
        var totalPop = 0.0
        for (i in 0 until nHours) {

            val hour = oneCall.hourly[i]
            totalPop += hour.pop
        }
        val pop = totalPop / nHours

        val now = ZonedDateTime.now()

        var sunriseSunsetElement: ForecastElement? = null
        for (i in oneCall.daily.indices) {

            val day = oneCall.daily[i]

            val sunrise = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(day.sunrise),
                ZoneOffset.UTC
            )
            if (sunrise.isAfter(now)) {

                sunriseSunsetElement = ForecastElement.Sunrise(sunrise)
                break
            } else {

                val sunset =
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(day.sunset), ZoneOffset.UTC)
                if (sunset.isAfter(now)) {

                    sunriseSunsetElement = ForecastElement.Sunset(sunset)
                    break
                }
            }
        }

        val elements = listOf(

            ForecastElement.UVIndex(uv.uv),
            ForecastElement.Pop(pop),
            ForecastElement.FeelsLike(
                ScaledTemperature.fromKelvin(oneCall.feelsLike).to(temperatureScale)
            ),
            ForecastElement.Humidity(oneCall.humidity),
            ForecastElement.WindSpeed(oneCall.windSpeed),
            sunriseSunsetElement!!
        )

        val hourSnapshots = oneCall.hourly.subList(0, 3).map {

            HourSnapshot(
                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
                OpenWeatherCodeMapper.map(it.conditionCode),
                false
            )
        }

        return CurrentForecast(
            ScaledTemperature.fromKelvin(
                oneCall.temp
            ).to(temperatureScale),
            ScaledTemperature.fromKelvin(
                today.tempLow
            ).to(temperatureScale),
            ScaledTemperature.fromKelvin(
                today.tempHigh
            ).to(temperatureScale),
            OpenWeatherCodeMapper.map(oneCall.conditionCode),
            elements,
            hourSnapshots
        )
    }
}