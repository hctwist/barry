package com.twisthenry8gmail.projectbarry.data

class DailyForecast(
    val tempLow: Temperature,
    val tempHigh: Temperature,
    val condition: WeatherCondition,
    val pop: Double,
    val hourlyForecast: List<HourlyForecast>
) {
}