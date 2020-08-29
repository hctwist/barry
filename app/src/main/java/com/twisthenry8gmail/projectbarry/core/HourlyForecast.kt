package com.twisthenry8gmail.projectbarry.core

import java.time.ZonedDateTime

open class HourlyForecast(val time: ZonedDateTime, val condition: WeatherCondition) {

    class Temperature(
        time: ZonedDateTime,
        condition: WeatherCondition,
        val temperature: ScaledTemperature
    ) : HourlyForecast(time, condition)

    class Pop(time: ZonedDateTime, condition: WeatherCondition, val pop: Double) :
        HourlyForecast(time, condition)
}