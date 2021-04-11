package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

class DailyForecast(val days: List<Day>) {

    class Day(val date: ZonedDateTime, val condition: WeatherCondition, val tempHigh: ScaledTemperature, val tempLow: ScaledTemperature, val uvIndex: Double, val pop: Double, val windSpeed: ScaledWindSpeed)
}