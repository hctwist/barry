package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.data.PremiumRepository
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.HourlyForecast
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.util.ForecastUtil
import java.time.ZoneId
import javax.inject.Inject

class GetHourlyPopForecast @Inject constructor(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        premiumRepository: PremiumRepository,
        private val forecastRepository: ForecastRepository
) : HourlyForecastUseCase<ForecastElement.Pop>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository, premiumRepository) {

    override suspend operator fun invoke(location: Location): Outcome<HourlyForecast<ForecastElement.Pop>> {

        return forecastRepository.get(location).map {

            buildPopForecast(it)
        }
    }

    private fun buildPopForecast(forecast: Forecast): HourlyForecast<ForecastElement.Pop> {

        val hours = forecast.hourly.mapFirst(numberOfHoursToFetch) {

            val time = it.time.atZone(ZoneId.systemDefault())
            HourlyForecast.Hour(
                    time,
                    ForecastUtil.isNight(time, forecast),
                    it.condition,
                    ForecastElement.Pop(it.pop)
            )
        }

        return HourlyForecast(hours, 0.0, 1.0, getChange(hours))
    }
}