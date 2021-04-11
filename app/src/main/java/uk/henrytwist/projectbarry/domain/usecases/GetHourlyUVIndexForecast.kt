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
import uk.henrytwist.projectbarry.domain.util.ForecastUtil
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.max

@ExperimentalCoroutinesApi
class GetHourlyUVIndexForecast @Inject constructor(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        premiumRepository: PremiumRepository,
        private val forecastRepository: ForecastRepository
) : HourlyForecastUseCase<ForecastElement.UVIndex>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository, premiumRepository) {

    override suspend operator fun invoke(location: Location): Outcome<HourlyForecast<ForecastElement.UVIndex>> {

        val forecast = forecastRepository.get(location)
        return forecast.map {

            buildUVIndexForecast(it)
        }
    }

    private fun buildUVIndexForecast(forecast: Forecast): HourlyForecast<ForecastElement.UVIndex> {

        val hours = forecast.hourly.mapFirst(numberOfHoursToFetch) {

            val time = it.time.atZone(ZoneId.systemDefault())
            HourlyForecast.Hour(
                    time,
                    ForecastUtil.isNight(time, forecast),
                    it.condition,
                    ForecastElement.UVIndex(it.uvIndex)
            )
        }

        val uvMax = hours.maxOf { it.element.index }
        val suggestedMax = max(uvMax, 7.0)

        return HourlyForecast(hours, 0.0, suggestedMax, getChange(hours))
    }
}