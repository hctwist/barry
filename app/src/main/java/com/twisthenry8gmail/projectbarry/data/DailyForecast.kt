package com.twisthenry8gmail.projectbarry.data

import java.time.ZonedDateTime

class DailyForecast(
    val day: ZonedDateTime,
    val tempLow: Temperature,
    val tempHigh: Temperature,
    val condition: WeatherCondition,
    val pop: Double,
    val hourlyForecast: List<HourlyForecast>
)