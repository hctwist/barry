package com.twisthenry8gmail.projectbarry.core

import java.time.ZonedDateTime

class HourlyForecast2(val hours: List<Hour>, val minValue: Double, val maxValue: Double) {

    class Hour(
        val time: ZonedDateTime,
        val weatherCondition: WeatherCondition,
        val element: ForecastElement,
        val value: Double
    )
}