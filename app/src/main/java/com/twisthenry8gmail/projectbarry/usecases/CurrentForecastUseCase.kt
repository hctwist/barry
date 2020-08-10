package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.*
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVRepository
import com.twisthenry8gmail.projectbarry.data.openuv.OpenUVSource
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.min

class CurrentForecastUseCase @Inject constructor(
    private val openWeatherRepository: OpenWeatherRepository,
    private val openUVRepository: OpenUVRepository
) {

    suspend fun getCurrentForecast(
        location: ForecastLocation,
        forceRefresh: Boolean
    ): Result<CurrentForecast> {

        val oneCallData = openWeatherRepository.getOneCallData(location, forceRefresh)
        val realTimeUVData = openUVRepository.getRealTimeUVData(location, forceRefresh)

        return if (oneCallData is Result.Success && realTimeUVData is Result.Success) {

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

    private fun buildCurrentForecast(
        oneCall: OpenWeatherSource.OneCallData,
        uv: OpenUVSource.RealTimeData
    ): CurrentForecast {

        val today = oneCall.daily[0]
        val hourly = oneCall.hourly.map {

            HourlyForecast(
                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
                Temperature.fromKelvin(
                    it.temp
                ),
                OpenWeatherCodeMapper.map(it.conditionCode),
                it.pop
            )
        }

        return CurrentForecast(
            Instant.ofEpochSecond(min(oneCall.time, uv.time)),
            Instant.ofEpochSecond(oneCall.sunset).atZone(ZoneId.systemDefault()),
            Instant.ofEpochSecond(oneCall.sunrise).atZone(ZoneId.systemDefault()),
            Temperature.fromKelvin(
                oneCall.temp
            ),
            Temperature.fromKelvin(
                today.tempLow
            ),
            Temperature.fromKelvin(
                today.tempHigh
            ),
            OpenWeatherCodeMapper.map(oneCall.conditionCode),
            uv.uv,
            Temperature.fromKelvin(
                oneCall.feelsLike
            ),
            hourly
        )
    }
}