package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.data.CurrentForecast
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVSource
import com.twisthenry8gmail.projectbarry.data.openuv.RealTimeUVRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import com.twisthenry8gmail.projectbarry.core.failure
import com.twisthenry8gmail.projectbarry.core.success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetNowForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository,
    private val realTimeUVRepository: RealTimeUVRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(location: ForecastLocation): Result<CurrentForecast> {

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

//        val lowerBound = ZonedDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS)
//
//        val morningBound = lowerBound.plusDays(1).withHour(4)
//        val twelveHourBound = lowerBound.plusHours(12)
//        val upperBound =
//            if (morningBound.isAfter(twelveHourBound)) morningBound else twelveHourBound
//
//        val hourly = oneCall.hourly.map {
//
//            HourlyForecast(
//                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
//                Temperature.fromKelvin(
//                    it.temp
//                ).to(temperatureScale),
//                OpenWeatherCodeMapper.map(it.conditionCode),
//                it.pop
//            )
//        }.filter {
//
//            !it.time.isAfter(upperBound) && !it.time.isBefore(lowerBound)
//        }

        // TODO, maybe next 2 hours or so worked out statistically?
        val pop = 0.0

        return CurrentForecast(
            Instant.ofEpochSecond(oneCall.sunset).atZone(ZoneId.systemDefault()),
            Instant.ofEpochSecond(oneCall.sunrise).atZone(ZoneId.systemDefault()),
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
            uv.uv,
            pop,
            ScaledTemperature.fromKelvin(
                oneCall.feelsLike
            ).to(temperatureScale),
            oneCall.humidity,
            oneCall.windSpeed
        )
    }
}