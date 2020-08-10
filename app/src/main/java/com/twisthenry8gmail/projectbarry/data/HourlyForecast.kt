package com.twisthenry8gmail.projectbarry.data

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class HourlyForecast(val time: ZonedDateTime, val temp: Temperature, val condition: WeatherCondition, val pop: Double)