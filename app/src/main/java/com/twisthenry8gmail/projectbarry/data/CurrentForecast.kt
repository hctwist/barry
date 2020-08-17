package com.twisthenry8gmail.projectbarry.data

import java.time.Instant
import java.time.ZonedDateTime

class CurrentForecast(
    val timestamp: Instant,
    val sunset: ZonedDateTime,
    val sunrise: ZonedDateTime,
    val temp: Temperature,
    val tempLow: Temperature,
    val tempHigh: Temperature,
    val condition: WeatherCondition,
    val uvIndex: Double,
    val feelsLike: Temperature,
    val humidity: Int,
    val windSpeed: Double,
    val hourly: List<HourlyForecast>
)