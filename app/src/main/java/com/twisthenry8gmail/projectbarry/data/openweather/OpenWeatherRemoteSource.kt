package com.twisthenry8gmail.projectbarry.data.openweather

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.failure
import com.twisthenry8gmail.projectbarry.data.APIKeyStore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OpenWeatherRemoteSource @Inject constructor(private val volleyRequestQueue: RequestQueue) :
    OpenWeatherSource {

    override suspend fun getOneCallData(lat: Double, lng: Double) =
        suspendCoroutine<Result<OpenWeatherSource.OneCallData>> { continuation ->

            volleyRequestQueue.add(
                JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lng}&exclude=minutely&appid=${APIKeyStore.getOpenWeatherKey()}",
                    null,
                    { root ->

                        val current = root.getJSONObject("current")

                        val time = current.getLong("dt")
                        val currentTemperature = current.getDouble("temp")
                        val feelsLike = current.getDouble("feels_like")
                        val humidity = current.getInt("humidity")
                        val windSpeed = current.getDouble("wind_speed")

                        val conditionCode =
                            current.getJSONArray("weather").getJSONObject(0).getInt("id")

                        val hourly = root.getJSONArray("hourly")
                        val hourlyData = List(hourly.length()) {

                            val hour = hourly.getJSONObject(it)
                            val hourTime = hour.getLong("dt")
                            val hourTemp = hour.getDouble("temp")
                            val hourConditionCode =
                                hour.getJSONArray("weather").getJSONObject(0).getInt("id")
                            val hourPop = hour.getDouble("pop")
                            OpenWeatherSource.OneCallData.Hour(
                                hourTime,
                                hourTemp,
                                hourConditionCode,
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
                            val dayPop = day.getDouble("pop")
                            val daySunrise = day.getLong("sunrise")
                            val daySunset = day.getLong("sunset")
                            OpenWeatherSource.OneCallData.Day(
                                dayTime,
                                dayTempLow,
                                dayTempHigh,
                                dayConditionCode,
                                dayPop,
                                daySunrise,
                                daySunset
                            )
                        }

                        val result =
                            OpenWeatherSource.OneCallData(
                                time,
                                lat,
                                lng,
                                currentTemperature,
                                conditionCode,
                                feelsLike,
                                humidity,
                                windSpeed,
                                hourlyData,
                                dailyData
                            )

                        continuation.resume(
                            Result.Success(
                                result
                            )
                        )
                    },
                    {

                        continuation.resume(failure())
                    })
            )
        }
}