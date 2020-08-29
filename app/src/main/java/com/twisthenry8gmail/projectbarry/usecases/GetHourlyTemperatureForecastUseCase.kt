package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.core.HourlyForecast
import com.twisthenry8gmail.projectbarry.data.SettingsRepository
import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherCodeMapper
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetHourlyTemperatureForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(location: ForecastLocation): Result<List<HourlyForecast.Temperature>> {

        val oneCallResult = oneCallRepository.get(location)

        return oneCallResult.map {

            buildTemperatureForecast(it)
        }
    }

    private fun buildTemperatureForecast(oneCallData: OpenWeatherSource.OneCallData): List<HourlyForecast.Temperature> {

        val temperatureScale = settingsRepository.getTemperatureScale()

        return oneCallData.hourly.map {

            HourlyForecast.Temperature(

                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
                OpenWeatherCodeMapper.map(it.conditionCode),
                ScaledTemperature.fromKelvin(it.temp).to(temperatureScale)
            )
        }
    }
}