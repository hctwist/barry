package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.application.data.settings.SettingsRepositoryImpl
import com.twisthenry8gmail.projectbarry.application.data.openweather.OneCallRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetHourlyTemperatureForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository,
    private val settingsRepository: SettingsRepositoryImpl
) {

//    suspend operator fun invoke(location: LocationData): Result<HourlyForecast2> {
//
//        val oneCallResult = oneCallRepository.get(location)
//
//        return oneCallResult.map {
//
//            buildTemperatureForecast(it)
//        }
//    }
//
//    private fun buildTemperatureForecast(oneCallData: OpenWeatherSource.OneCallData): HourlyForecast2 {
//
//        val temperatureScale = settingsRepository.getTemperatureScale()
//
//        val hours = oneCallData.hourly.map {
//
//            val temp = ScaledTemperature.fromKelvin(it.temp).to(temperatureScale)
//            HourlyForecast2.Hour(
//
//                Instant.ofEpochSecond(it.time).atZone(ZoneId.systemDefault()),
//                OpenWeatherCodeMapper.map(it.conditionCode),
//                ForecastElement.Temperature(temp),
//                temp.value
//            )
//        }
//
//        // TODO Have better minimum value selection, either country based or even just rounded to nearest 5 etc.
//        val suggestedMinValue =
//            ScaledTemperature(0.0, ScaledTemperature.Scale.CELSIUS).to(temperatureScale).value
//        val suggestedMaxValue =
//            ScaledTemperature(30.0, ScaledTemperature.Scale.CELSIUS).to(temperatureScale).value
//
//        val hourMinValue = hours.minOf { it.value }
//        val hourMaxValue = hours.maxOf { it.value }
//
//        val minValue = min(suggestedMinValue, hourMinValue)
//        val maxValue = max(suggestedMaxValue, hourMaxValue)
//
//        return HourlyForecast2(hours, minValue, maxValue)
//    }
}