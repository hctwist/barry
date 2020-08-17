package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.CurrentForecast
import com.twisthenry8gmail.projectbarry.data.HourlyForecast
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.data.Temperature
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVRepository
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVSource
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import com.twisthenry8gmail.projectbarry.failure
import com.twisthenry8gmail.projectbarry.success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.min

@ExperimentalCoroutinesApi
class GetCurrentForecastUseCase @Inject constructor(
    private val openWeatherRepository: OpenWeatherRepository,
    private val openUVRepository: OpenUVRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(
        location: ForecastLocation,
        forceRefresh: Boolean
    ): Result<CurrentForecast> {

        val forecast = fetchCurrentForecast(location, forceRefresh)

        return if (!forceRefresh && forecast is Result.Success && ChronoUnit.SECONDS.between(
                forecast.data.timestamp,
                Instant.now()
            ) > settingsRepository.getRefreshInterval()
        ) {

            fetchCurrentForecast(location, true)
        } else {

            forecast
        }
    }

    private suspend fun fetchCurrentForecast(
        location: ForecastLocation,
        forceRefresh: Boolean
    ): Result<CurrentForecast> {

        return coroutineScope {

            val oneCallDataJob =
                async { openWeatherRepository.getOneCallData(location, forceRefresh) }
            val realTimeUVDataJob =
                async { openUVRepository.getRealTimeUVData(location, forceRefresh) }

            val oneCallData = oneCallDataJob.await()
            val realTimeUVData = realTimeUVDataJob.await()

            if (oneCallData is Result.Success && realTimeUVData is Result.Success) {

                success(
                    buildCurrentForecast(
                        oneCallData.data,
                        realTimeUVData.data
                    )
                )
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

        val lowerBound = ZonedDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS)

        val morningBound = lowerBound.plusDays(1).withHour(4)
        val twelveHourBound = lowerBound.plusHours(12)
        val upperBound =
            if (morningBound.isAfter(twelveHourBound)) morningBound else twelveHourBound

        val hourly = oneCall.hourly.map {

            HourlyForecast(
                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
                Temperature.fromKelvin(
                    it.temp
                ).to(temperatureScale),
                OpenWeatherCodeMapper.map(it.conditionCode),
                it.pop
            )
        }.filter {

            !it.time.isAfter(upperBound) && !it.time.isBefore(lowerBound)
        }

        return CurrentForecast(
            Instant.ofEpochSecond(min(oneCall.time, uv.time)),
            Instant.ofEpochSecond(oneCall.sunset).atZone(ZoneId.systemDefault()),
            Instant.ofEpochSecond(oneCall.sunrise).atZone(ZoneId.systemDefault()),
            Temperature.fromKelvin(
                oneCall.temp
            ).to(temperatureScale),
            Temperature.fromKelvin(
                today.tempLow
            ).to(temperatureScale),
            Temperature.fromKelvin(
                today.tempHigh
            ).to(temperatureScale),
            OpenWeatherCodeMapper.map(oneCall.conditionCode),
            uv.uv,
            Temperature.fromKelvin(
                oneCall.feelsLike
            ).to(temperatureScale),
            oneCall.humidity,
            oneCall.windSpeed,
            hourly
        )
    }
}