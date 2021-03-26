package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.projectbarry.domain.data.PremiumRepository
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.HourlyForecast
import uk.henrytwist.projectbarry.domain.models.Location
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.max

@ExperimentalCoroutinesApi
class GetHourlyTemperatureForecast @Inject constructor(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        premiumRepository: PremiumRepository,
        private val forecastRepository: ForecastRepository,
        private val settingsRepository: SettingsRepository
) : HourlyForecastUseCase<ForecastElement.Temperature>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository, premiumRepository) {

    override suspend operator fun invoke(location: Location): Outcome<HourlyForecast<ForecastElement.Temperature>> {

        val forecast = forecastRepository.get(location)
        return forecast.map {

            buildTemperatureForecast(it)
        }
    }

    private fun buildTemperatureForecast(forecast: Forecast): HourlyForecast<ForecastElement.Temperature> {

        val temperatureScale = settingsRepository.getTemperatureScale()

        val hours = forecast.hourly.mapFirst(numberOfHoursToFetch) {

            val temp = it.temp.to(temperatureScale)
            HourlyForecast.Hour(

                    it.time.atZone(ZoneId.systemDefault()),
                    it.condition,
                    ForecastElement.Temperature(temp)
            )
        }

        var suggestedMin = hours.minOf { it.element.temperature.celsius() }
        var suggestedMax = hours.maxOf { it.element.temperature.celsius() }

        val minRange = 10
        val rangeIncrease = max(minRange - (suggestedMax - suggestedMin), 5.0)
        val maxIncreaseWeight = 0F
        val minIncreaseWeight = 1 - maxIncreaseWeight

        suggestedMin -= rangeIncrease * minIncreaseWeight
        suggestedMax += rangeIncrease * maxIncreaseWeight

        return HourlyForecast(hours, suggestedMin, suggestedMax, getChange(hours))
    }
}