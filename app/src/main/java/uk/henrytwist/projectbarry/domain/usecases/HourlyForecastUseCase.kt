package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.data.PremiumRepository
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.ConditionChange
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.HourlyForecast
import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.min

abstract class HourlyForecastUseCase<FE : ForecastElement>(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        private val premiumRepository: PremiumRepository
) : LocationUseCase<HourlyForecast<FE>>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository) {

    protected val numberOfHoursToFetch
        get() = if (premiumRepository.hasPremium()) 48 else 24

    protected inline fun <T, R> List<T>.mapFirst(n: Int, transform: (T) -> R): List<R> {

        val safeN = min(n, size)
        val newList = ArrayList<R>(safeN)
        for (i in 0 until safeN) {

            newList.add(transform(this[i]))
        }

        return newList
    }

    protected fun getChange(hours: List<HourlyForecast.Hour<*>>): ConditionChange {

        val now = LocalDate.now()

        val currentCondition = hours.first().condition

        for (hour in hours) {

            if (hour.condition.group != currentCondition.group) {

                val dt = now.until(hour.time.toLocalDate(), ChronoUnit.DAYS)

                return when {

                    dt >= 2 -> {

                        ConditionChange.AllDay(currentCondition)
                    }
                    hour.condition.group == WeatherCondition.Group.CLEAR -> {

                        ConditionChange.Until(hour.time, currentCondition, dt == 1L)
                    }
                    dt == 1L -> {

                        ConditionChange.Tomorrow(currentCondition, hour.condition, hour.time)
                    }
                    else -> {

                        ConditionChange.At(hour.time, hour.condition)
                    }
                }
            }
        }

        return ConditionChange.AllDay(currentCondition)
    }
}