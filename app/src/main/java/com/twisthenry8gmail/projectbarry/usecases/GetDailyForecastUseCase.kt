package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetDailyForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(location: ForecastLocation): Result<List<DailyForecast>> {

        val oneCallData = oneCallRepository.get(location)
        val temperatureScale = settingsRepository.getTemperatureScale()

        return oneCallData.map { oneCall ->

            oneCall.daily.map {

                DailyForecast(
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.time), ZoneId.systemDefault()),
                    ScaledTemperature.fromKelvin(it.tempLow).to(temperatureScale),
                    ScaledTemperature.fromKelvin(it.tempHigh).to(temperatureScale),
                    OpenWeatherCodeMapper.map(it.conditionCode),
                    it.pop
                )
            }
        }
    }
}