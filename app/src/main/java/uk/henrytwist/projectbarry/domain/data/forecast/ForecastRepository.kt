package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.kotlinbasics.scanBy
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel
import uk.henrytwist.projectbarry.domain.data.KeyedCacheRepository
import uk.henrytwist.projectbarry.domain.data.toInstant
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

class ForecastRepository @Inject constructor(private val localSource: ForecastLocalSource, private val remoteSource: ForecastRemoteSource) : KeyedCacheRepository<LocationCoordinates, Forecast>() {

    private val distanceTolerance = 250.0
    private val timeTolerance = 900L

    private var memoryCache = LinkedList<Forecast>()

    override suspend fun saveLocal(value: Forecast) {

        saveInMemory(value)
        localSource.save(value.toModel())
    }

    override suspend fun fetchLocal(key: LocationCoordinates): Outcome<Forecast> {

        val memory = fetchInMemory(key)
        if (memory is Outcome.Success) {

            return memory
        }

        val afterTime = Instant.now().minus(timeTolerance, ChronoUnit.SECONDS)
        return localSource.get(afterTime, key, distanceTolerance).map { value ->

            val forecast = value.toForecast { WeatherCondition.values()[it] }
            saveInMemory(forecast)
            forecast
        }
    }

    private fun fetchInMemory(key: LocationCoordinates): Outcome<Forecast> {

        val now = Instant.now()
        memoryCache.removeIf { it.time.until(now, ChronoUnit.SECONDS) > timeTolerance }

        return memoryCache.scanBy { it.coordinates.distanceTo(key) }
                .filter { it < distanceTolerance }
                .min()?.asSuccess() ?: failure()
    }

    private fun saveInMemory(forecast: Forecast) {

        memoryCache.add(forecast)
    }

    override suspend fun fetchRemote(key: LocationCoordinates): Outcome<Forecast> {

        return remoteSource.get(key).map { value ->
            value.toForecast { ConditionCodeMapper.map(it) }
        }
    }

    suspend fun cleanup() {

        localSource.delete(Instant.now().minus(timeTolerance, ChronoUnit.SECONDS))
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
                coordinates,
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
                coordinates,
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
