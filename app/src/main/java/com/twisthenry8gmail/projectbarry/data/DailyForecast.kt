package com.twisthenry8gmail.projectbarry.data

import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.core.WeatherCondition
import java.time.ZonedDateTime

class DailyForecast(
    val day: ZonedDateTime,
    val tempLow: ScaledTemperature,
    val tempHigh: ScaledTemperature,
    val condition: WeatherCondition,
    val pop: Double
)