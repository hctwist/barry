package com.twisthenry8gmail.projectbarry.data

import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.core.WeatherCondition
import java.time.ZonedDateTime

class CurrentForecast(
    val sunset: ZonedDateTime,
    val sunrise: ZonedDateTime,
    val temp: ScaledTemperature,
    val tempLow: ScaledTemperature,
    val tempHigh: ScaledTemperature,
    val condition: WeatherCondition,
    val uvIndex: Double,
    val pop: Double,
    val feelsLike: ScaledTemperature,
    val humidity: Int,
    val windSpeed: Double,
)