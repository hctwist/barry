package uk.henrytwist.projectbarry.application.data.forecast

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.asSuccess
import uk.henrytwist.kotlinbasics.failure
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastLocalSource
import javax.inject.Inject

class ForecastLocalSourceImpl @Inject constructor(
        private val dao: ForecastDao
) : ForecastLocalSource {

    override suspend fun get(placeId: String): Outcome<ForecastModel> {

        val entity = dao.get(placeId) ?: return failure()

        return entity.let { forecast ->

            val hours = forecast.hourForecastEntities.map {

                ForecastModel.Hour(
                        it.time,
                        it.temp,
                        it.conditionCode,
                        it.pop
                )
            }

            val days = forecast.dayForecastEntities.map {

                ForecastModel.Day(
                        it.time,
                        it.tempLow,
                        it.tempHigh,
                        it.conditionCode,
                        it.pop,
                        it.sunrise,
                        it.sunset
                )
            }

            val cf = forecast.currentForecastEntity
            ForecastModel(
                    cf.placeId,
                    cf.time,
                    cf.temp,
                    cf.conditionCode,
                    cf.feelsLike,
                    cf.humidity,
                    cf.windSpeed,
                    hours,
                    days
            )
        }.asSuccess()
    }

    override suspend fun save(forecast: ForecastModel) {

        dao.insert(forecast.let { model ->

            val currentForecastEntity = CurrentForecastEntity(
                    model.placeId,
                    model.time,
                    model.temp,
                    model.conditionCode,
                    model.feelsLike,
                    model.humidity,
                    model.windSpeed
            )

            val hourForecastEntities = model.hourly.map {

                HourForecastEntity(
                        model.placeId,
                        it.time,
                        it.temp,
                        it.conditionCode,
                        it.pop
                )
            }

            val dayForecastEntities = model.daily.map {

                DayForecastEntity(
                        model.placeId,
                        it.time,
                        it.tempLow,
                        it.tempHigh,
                        it.conditionCode,
                        it.pop,
                        it.sunrise,
                        it.sunset
                )
            }

            ForecastEntity(currentForecastEntity, hourForecastEntities, dayForecastEntities)
        })
    }
}