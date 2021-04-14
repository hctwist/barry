package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.coroutineScope
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.*
import uk.henrytwist.projectbarry.domain.util.ForecastUtil
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetNowForecast @Inject constructor(
        selectedLocationRepository: SelectedLocationRepository,
        currentLocationRepository: CurrentLocationRepository,
        savedLocationsRepository: SavedLocationsRepository,
        private val forecastRepository: ForecastRepository,
        private val settingsRepository: SettingsRepository
) : LocationUseCase<NowForecast>(selectedLocationRepository, currentLocationRepository, savedLocationsRepository) {

    override suspend fun invoke(location: Location): Outcome<NowForecast> {

        return coroutineScope {

            val forecast = forecastRepository.get(location)

            forecast.map {

                buildCurrentForecast(it)
            }
        }
    }

    private fun buildCurrentForecast(forecast: Forecast): NowForecast {

        val temperatureScale = settingsRepository.getTemperatureScale()
        val windSpeedScale = settingsRepository.getWindSpeedScale()

        val nPopHours = 2
        var totalPop = 0.0
        for (i in 0 until nPopHours) {

            val hour = forecast.hourly[i]
            totalPop += hour.pop
        }
        val pop = totalPop / nPopHours

        val elements = listOf(

                ForecastElement.Pop(pop),
                ForecastElement.UVIndex(forecast.uvIndex),
                ForecastElement.WindSpeed(forecast.windSpeed.to(windSpeedScale)),
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
        val daySnapshot = NowForecast.DaySnapshot(
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
                ForecastUtil.isNight(ZonedDateTime.now(), forecast),
                forecast.temp.to(temperatureScale),
                forecast.feelsLike.to(temperatureScale),
                elements,
                daySnapshot,
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