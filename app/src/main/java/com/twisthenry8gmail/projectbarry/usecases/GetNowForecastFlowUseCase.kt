package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.*
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.data.locations2.ForecastLocationRepository2
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVSource
import com.twisthenry8gmail.projectbarry.data.openuv.RealTimeUVRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetNowForecastFlowUseCase @Inject constructor(
    private val locationRepository: ForecastLocationRepository2,
    private val oneCallRepository: OneCallRepository,
    private val realTimeUVRepository: RealTimeUVRepository,
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Result<CurrentForecast>> {

        return locationRepository.selectedLocation.map { locationResult ->

            locationResult.switchMap { location ->

                val locationData = location.locationData

                if (locationData == null) {

                    waiting()
                } else {

                    coroutineScope {

                        val oneCallDataJob = async { oneCallRepository.get(locationData) }
                        val realTimeUVDataJob = async { realTimeUVRepository.get(locationData) }

                        val oneCallData = oneCallDataJob.await()
                        val realTimeUVData = realTimeUVDataJob.await()

                        if (oneCallData is Result.Success && realTimeUVData is Result.Success) {

                            success(buildCurrentForecast(oneCallData.data, realTimeUVData.data))
                        } else {

                            failure()
                        }
                    }
                }
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

        // TODO Elements from settings
        val elements = listOf(

            ForecastElement.UVIndex(uv.uv),
            ForecastElement.Pop(pop),
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