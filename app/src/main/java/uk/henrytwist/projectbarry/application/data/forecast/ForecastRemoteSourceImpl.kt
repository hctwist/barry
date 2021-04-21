package uk.henrytwist.projectbarry.application.data.forecast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import uk.henrytwist.kotlinbasics.outcomes.NetworkFailure
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.projectbarry.domain.data.APIKeyStore
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRemoteSource
import uk.henrytwist.projectbarry.domain.models.Location
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ForecastRemoteSourceImpl @Inject constructor(private val volleyRequestQueue: RequestQueue) :
        ForecastRemoteSource {

    override suspend fun get(location: Location) =
            suspendCoroutine<Outcome<ForecastModel>> { continuation ->

                volleyRequestQueue.add(
                        JsonObjectRequest(
                                Request.Method.GET,
                                "https://api.openweathermap.org/data/2.5/onecall?lat=${location.coordinates.lat}&lon=${location.coordinates.lng}&exclude=minutely&appid=${APIKeyStore.getOpenWeatherKey()}",
                                null,
                                { root ->

                                    val current = root.getJSONObject("current")

                                    val time = current.getLong("dt")
                                    val currentTemperature = current.getDouble("temp")
                                    val feelsLike = current.getDouble("feels_like")
                                    val uvIndex = current.getDouble("uvi")
                                    val dewPoint = current.getDouble("dew_point")
                                    val windSpeed = current.getDouble("wind_speed")

                                    val conditionCode =
                                            current.getJSONArray("weather").getJSONObject(0).getInt("id")

                                    val hourly = root.getJSONArray("hourly")
                                    val hourlyData = List(hourly.length()) {

                                        val hour = hourly.getJSONObject(it)
                                        val hourTime = hour.getLong("dt")
                                        val hourTemp = hour.getDouble("temp")
                                        val hourConditionCode = hour.getJSONArray("weather").getJSONObject(0).getInt("id")
                                        val hourUVIndex = hour.getDouble("uvi")
                                        val hourWindSpeed = hour.getDouble("wind_speed")
                                        val hourPop = hour.getDouble("pop")
                                        ForecastModel.Hour(
                                                hourTime,
                                                hourTemp,
                                                hourConditionCode,
                                                hourUVIndex,
                                                hourWindSpeed,
                                                hourPop
                                        )
                                    }

                                    val daily = root.getJSONArray("daily")
                                    val dailyData = List(daily.length()) {

                                        val day = daily.getJSONObject(it)
                                        val dayTime = day.getLong("dt")
                                        val dayTemp = day.getJSONObject("temp")
                                        val dayTempLow = dayTemp.getDouble("min")
                                        val dayTempHigh = dayTemp.getDouble("max")
                                        val dayConditionCode =
                                                day.getJSONArray("weather").getJSONObject(0).getInt("id")
                                        val dayUVIndex = day.getDouble("uvi")
                                        val dayPop = day.getDouble("pop")
                                        val dayWindSpeed = day.getDouble("wind_speed")
                                        val daySunrise = day.getLong("sunrise")
                                        val daySunset = day.getLong("sunset")
                                        ForecastModel.Day(
                                                dayTime,
                                                dayTempLow,
                                                dayTempHigh,
                                                dayConditionCode,
                                                dayUVIndex,
                                                dayPop,
                                                dayWindSpeed,
                                                daySunrise,
                                                daySunset
                                        )
                                    }

                                    val result = ForecastModel(
                                            location.placeId,
                                            time,
                                            currentTemperature,
                                            conditionCode,
                                            feelsLike,
                                            uvIndex,
                                            dewPoint,
                                            windSpeed,
                                            hourlyData,
                                            dailyData
                                    )

                                    continuation.resume(result.asSuccess())
                                },
                                {

                                    continuation.resume(NetworkFailure())
                                })
                )
            }
}