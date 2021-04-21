package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel
import uk.henrytwist.projectbarry.domain.data.KeyedCacheRepository
import uk.henrytwist.projectbarry.domain.data.toInstant
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ForecastRepository @Inject constructor(private val localSource: ForecastLocalSource, private val remoteSource: ForecastRemoteSource) : KeyedCacheRepository<Location, Forecast>() {

    override suspend fun isStale(value: Forecast): Boolean {

        // TODO Decide on value
        return value.time.until(Instant.now(), ChronoUnit.MINUTES) > 60
    }

    override suspend fun saveLocal(value: Forecast) {

        localSource.save(value.toModel())
    }

    override suspend fun fetchLocal(key: Location): Outcome<Forecast> {

        return localSource.get(key.placeId).map { value ->
            value.toForecast { WeatherCondition.values()[it] }
        }
    }

    override suspend fun fetchRemote(key: Location): Outcome<Forecast> {

        return remoteSource.get(key).map { value ->
            value.toForecast { ConditionCodeMapper.map(it) }
        }
    }

    private fun Forecast.toModel(): ForecastModel {

        val hours = hourly.map {

            ForecastModel.Hour(
                    it.time.epochSecond,
                    it.temp.kelvin(),
                    it.condition.ordinal,
                    it.uvIndex,
                    it.windSpeed.metresPerSecond(),
                    it.pop
            )
        }

        val days = daily.map {

            ForecastModel.Day(
                    it.time.epochSecond,
                    it.tempLow.kelvin(),
                    it.tempHigh.kelvin(),
                    it.condition.ordinal,
                    it.uvIndex,
                    it.pop,
                    it.windSpeed.metresPerSecond(),
                    it.sunrise.epochSecond,
                    it.sunset.epochSecond
            )
        }

        return ForecastModel(
                placeId,
                time.epochSecond,
                temp.kelvin(),
                condition.ordinal,
                feelsLike.kelvin(),
                uvIndex,
                dewPoint.kelvin(),
                windSpeed.metresPerSecond(),
                hours,
                days
        )
    }

    private inline fun ForecastModel.toForecast(conditionCodeMapper: (Int) -> WeatherCondition): Forecast {

        val hours = hourly.map {

            Forecast.Hour(
                    it.time.toInstant(),
                    conditionCodeMapper(it.conditionCode),
                    ScaledTemperature.fromKelvin(it.temp),
                    it.uvIndex,
                    ScaledSpeed.fromMetresPerSecond(it.windSpeed),
                    it.pop
            )
        }

        val days = daily.map {

            Forecast.Day(
                    it.time.toInstant(),
                    ScaledTemperature.fromKelvin(it.tempLow),
                    ScaledTemperature.fromKelvin(it.tempHigh),
                    conditionCodeMapper(it.conditionCode),
                    it.uvIndex,
                    it.pop,
                    ScaledSpeed.fromMetresPerSecond(it.windSpeed),
                    it.sunrise.toInstant(),
                    it.sunset.toInstant()
            )
        }

        return Forecast(
                placeId,
                time.toInstant(),
                ScaledTemperature.fromKelvin(temp),
                conditionCodeMapper(conditionCode),
                ScaledTemperature.fromKelvin(feelsLike),
                uvIndex,
                ScaledTemperature.fromKelvin(dewPoint),
                ScaledSpeed.fromMetresPerSecond(windSpeed),
                hours,
                days
        )
    }
}