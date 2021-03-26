package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.failure
import uk.henrytwist.kotlinbasics.success
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.data.uv.UVRepository
import uk.henrytwist.projectbarry.domain.models.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetNowForecast @Inject constructor(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        private val forecastRepository: ForecastRepository,
        private val UVRepository: UVRepository,
        private val settingsRepository: SettingsRepository
) : LocationUseCase<NowForecast>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository) {

    override suspend fun invoke(location: Location): Outcome<NowForecast> {

        return coroutineScope {

            val forecastJob = async { forecastRepository.get(location) }
            val uvJob = async { UVRepository.get(location) }

            val forecast = forecastJob.await()
            val uv = uvJob.await()

            if (forecast is Outcome.Success && uv is Outcome.Success) {

                success(buildCurrentForecast(forecast.data, uv.data))
            } else {

                failure()
            }
        }
    }

    private fun buildCurrentForecast(
            forecast: Forecast,
            uv: UV
    ): NowForecast {

        val temperatureScale = settingsRepository.getTemperatureScale()

        val nPopHours = 2
        var totalPop = 0.0
        for (i in 0 until nPopHours) {

            val hour = forecast.hourly[i]
            totalPop += hour.pop
        }
        val pop = totalPop / nPopHours

        val elements = listOf(

                ForecastElement.Pop(pop),
                ForecastElement.UVIndex(uv.uv),
                ForecastElement.WindSpeed(forecast.windSpeed),
                ForecastElement.Humidity(forecast.humidity)
        )

        val nHourSnapshots = 12
        val hourSnapshots = List(nHourSnapshots) {

            val hour = forecast.hourly[it]
            NowForecast.HourSnapshot(hour.time.atZone(ZoneId.systemDefault()), hour.condition)
        }

        val today = forecast.daily.first()
        val shouldShowToday = Instant.now().isBefore(today.sunset)
        val day = if (shouldShowToday) today else forecast.daily[1]
        val todaySnapshot = NowForecast.DaySnapshot(
                shouldShowToday,
                day.condition,
                day.tempLow.to(temperatureScale),
                day.tempHigh.to(temperatureScale),
                day.sunrise.atZone(ZoneId.systemDefault()),
                day.sunset.atZone(ZoneId.systemDefault())
        )

        return NowForecast(
                forecast.condition,
                computeConditionChange(hourSnapshots),
                forecast.temp.to(temperatureScale),
                forecast.feelsLike.to(temperatureScale),
                elements,
                todaySnapshot,
                hourSnapshots
        )
    }

    private fun computeConditionChange(hourSnapshots: List<NowForecast.HourSnapshot>): ConditionChange {

        val now = LocalDate.now()

        val currentCondition = hourSnapshots.first().condition

        for (hour in hourSnapshots) {

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