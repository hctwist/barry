package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.models.DailyForecast
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import java.time.ZoneId
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetDailyForecast @Inject constructor(
        private val locationUseCaseHelper: LocationUseCaseHelper,
        private val forecastRepository: ForecastRepository,
        private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): Outcome<DailyForecast> {

        return locationUseCaseHelper.getLocation().switchMap { loc ->

            forecastRepository.get(loc).map {

                build(it)
            }
        }
    }

    private fun build(forecast: Forecast): DailyForecast {

        val temperatureScale = settingsRepository.getTemperatureScale()
        val windSpeedScale = settingsRepository.getSpeedScale()

        val days = forecast.daily.map {

            DailyForecast.Day(
                    it.time.atZone(ZoneId.systemDefault()),
                    it.condition,
                    it.tempHigh.to(temperatureScale),
                    it.tempLow.to(temperatureScale),
                    listOf(
                            ForecastElement.UVIndex(it.uvIndex),
                            ForecastElement.Pop(it.pop),
                            ForecastElement.WindSpeed(it.windSpeed.to(windSpeedScale))
                    )
            )
        }

        return DailyForecast(days)
    }
}