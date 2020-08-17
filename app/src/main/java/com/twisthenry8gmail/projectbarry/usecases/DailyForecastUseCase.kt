package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.data.Temperature
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class DailyForecastUseCase @Inject constructor(private val openWeatherRepository: OpenWeatherRepository, private val settingsRepository: SettingsRepository) {

    suspend fun getDailyForecasts(location: ForecastLocation, forceRefresh: Boolean): Result<List<DailyForecast>> {

        val oneCallData = openWeatherRepository.getOneCallData(location, forceRefresh)
        val temperatureScale = settingsRepository.getTemperatureScale()

        return oneCallData.map { oneCall ->

            oneCall.daily.map {

                DailyForecast(
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.time), ZoneId.systemDefault()),
                    Temperature.fromKelvin(it.tempLow).to(temperatureScale),
                    Temperature.fromKelvin(it.tempHigh).to(temperatureScale),
                    OpenWeatherCodeMapper.map(it.conditionCode),
                    it.pop,
                    listOf()
                )
            }
        }
    }
}