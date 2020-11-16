package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.*
import com.twisthenry8gmail.projectbarry.application.data.settings.SettingsRepositoryImpl
import com.twisthenry8gmail.projectbarry.application.data.openweather.OneCallRepository
import com.twisthenry8gmail.projectbarry.application.data.openweather.OpenWeatherCodeMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetDailyForecastUseCase @Inject constructor(
    private val oneCallRepository: OneCallRepository,
    private val settingsRepository: SettingsRepositoryImpl
) {

    suspend operator fun invoke(location: LocationData): Result<List<DaySnapshot>> {

        val oneCallData = oneCallRepository.get(location)
        val temperatureScale = settingsRepository.getTemperatureScale()

        return oneCallData.map { oneCall ->

            oneCall.daily.map {

                DaySnapshot(
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