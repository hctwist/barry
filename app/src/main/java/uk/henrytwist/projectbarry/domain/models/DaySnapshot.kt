package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

class DaySnapshot(
    val day: ZonedDateTime,
    val tempLow: ScaledTemperature,
    val tempHigh: ScaledTemperature,
    val condition: WeatherCondition,
    val pop: Double
)