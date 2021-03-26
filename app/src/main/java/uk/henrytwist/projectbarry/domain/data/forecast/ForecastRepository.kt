package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel
import uk.henrytwist.projectbarry.domain.data.toInstant
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import uk.henrytwist.projectbarry.domain.data.KeyedCacheRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ForecastRepository @Inject constructor(private val localSource: ForecastLocalSource, private val remoteSource: ForecastRemoteSource) : KeyedCacheRepository<Location, Forecast>() {

    override suspend fun isStale(value: Forecast): Boolean {

        // TODO Decide on value
        return value.time.until(Instant.now(), ChronoUnit.MINUTES) > 360
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
                    it.pop
            )
        }

        val days = daily.map {

            ForecastModel.Day(
                    it.time.epochSecond,
                    it.tempLow.kelvin(),
                    it.tempHigh.kelvin(),
                    it.condition.ordinal,
                    it.pop,
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
                humidity,
                windSpeed,
                hours,
                days
        )
    }

    private inline fun ForecastModel.toForecast(conditionCodeMapper: (Int) -> WeatherCondition): Forecast {

        val hours = hourly.map {

            Forecast.Hour(
                    it.time.toInstant(),
                    ScaledTemperature.fromKelvin(it.temp),
                    conditionCodeMapper(it.conditionCode),
                    it.pop
            )
        }

        val days = daily.map {

            Forecast.Day(
                    it.time.toInstant(),
                    ScaledTemperature.fromKelvin(it.tempLow),
                    ScaledTemperature.fromKelvin(it.tempHigh),
                    conditionCodeMapper(it.conditionCode),
                    it.pop,
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
                humidity,
                windSpeed,
                hours,
                days
        )
    }
}