package com.twisthenry8gmail.projectbarry.core

import java.time.ZonedDateTime

class DaySnapshot(
    val day: ZonedDateTime,
    val tempLow: ScaledTemperature,
    val tempHigh: ScaledTemperature,
    val condition: WeatherCondition,
    val pop: Double
)