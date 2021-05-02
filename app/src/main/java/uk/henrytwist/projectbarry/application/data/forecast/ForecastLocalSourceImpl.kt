package uk.henrytwist.projectbarry.application.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.kotlinbasics.scanBy
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastLocalSource
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import java.time.Instant
import javax.inject.Inject

class ForecastLocalSourceImpl @Inject constructor(
        private val dao: ForecastDao
) : ForecastLocalSource {

    override suspend fun get(afterTime: Instant, coordinates: LocationCoordinates, distanceTolerance: Double): Outcome<ForecastModel> {

        val candidateCoordinates = dao.getCoordinates(afterTime.epochSecond)

        val match = candidateCoordinates
                .scanBy { coordinates.distanceTo(LocationCoordinates(it.lat, it.lng)) }
                .filter { it < distanceTolerance }
                .min() ?: return failure()

        val result = dao.get(match.id)

        return result.let { forecast ->

            val hours = forecast.hourForecastEntities.map {

                ForecastModel.Hour(
                        it.time,
                        it.temp,
                        it.conditionCode,
                        it.uvIndex,
                        it.uvIndex,
                        it.pop
                )
            }

            val days = forecast.dayForecastEntities.map {

                ForecastModel.Day(
                        it.time,
                        it.tempLow,
                        it.tempHigh,
                        it.conditionCode,
                        it.uvIndex,
                        it.pop,
                        it.windSpeed,
                        it.sunrise,
                        it.sunset
                )
            }

            val cf = forecast.currentForecastEntity
            ForecastModel(
                    LocationCoordinates(cf.lat, cf.lng),
                    cf.time,
                    cf.temp,
                    cf.conditionCode,
                    cf.feelsLike,
                    cf.uvIndex,
                    cf.dewPoint,
                    cf.windSpeed,
                    hours,
                    days
            )
        }.asSuccess()
    }

    override suspend fun save(forecast: ForecastModel) {

        dao.insert(forecast.let { model ->

            val currentForecastEntity = CurrentForecastEntity(
                    0,
                    model.coordinates.lat,
                    model.coordinates.lng,
                    model.time,
                    model.temp,
                    model.conditionCode,
                    model.feelsLike,
                    model.uvIndex,
                    model.dewPoint,
                    model.windSpeed
            )

            val hourForecastEntities = model.hourly.map {

                HourForecastEntity(
                        0,
                        it.time,
                        it.temp,
                        it.conditionCode,
                        it.uvIndex,
                        it.windSpeed,
                        it.pop
                )
            }

            val dayForecastEntities = model.daily.map {

                DayForecastEntity(
                        0,
                        it.time,
                        it.tempLow,
                        it.tempHigh,
                        it.conditionCode,
                        it.uvIndex,
                        it.pop,
                        it.windSpeed,
                        it.sunrise,
                        it.sunset
                )
            }

            ForecastEntity(currentForecastEntity, hourForecastEntities, dayForecastEntities)
        })
    }

    override suspend fun delete(beforeTime: Instant) {

        dao.deleteForecasts(beforeTime.epochSecond)
    }
}