package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.DailyForecast
import uk.henrytwist.projectbarry.domain.models.Location
import java.time.ZoneId
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetDailyForecast @Inject constructor(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        private val forecastRepository: ForecastRepository,
        private val settingsRepository: SettingsRepository
) : LocationUseCase<DailyForecast>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository) {

    override suspend operator fun invoke(location: Location): Outcome<DailyForecast> {

        val forecast = forecastRepository.get(location)

        return forecast.map {

            build(it)
        }
    }

    private fun build(forecast: Forecast): DailyForecast {

        val temperatureScale = settingsRepository.getTemperatureScale()
        val windSpeedScale = settingsRepository.getWindSpeedScale()

        val days = forecast.daily.map {

            DailyForecast.Day(
                    it.time.atZone(ZoneId.systemDefault()),
                    it.condition,
                    it.tempHigh.to(temperatureScale),
                    it.tempLow.to(temperatureScale),
                    it.uvIndex,
                    it.pop,
                    it.windSpeed.to(windSpeedScale)
            )
        }

        return DailyForecast(days)
    }
}