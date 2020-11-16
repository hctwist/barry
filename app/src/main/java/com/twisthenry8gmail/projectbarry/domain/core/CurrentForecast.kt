package com.twisthenry8gmail.projectbarry.domain.core

class CurrentForecast(
    val temp: ScaledTemperature,
    val tempLow: ScaledTemperature,
    val tempHigh: ScaledTemperature,
    val condition: WeatherCondition,
    val elements: List<ForecastElement>,
    val hourSnapshots: List<HourSnapshot>,
    val daySnapshots: List<DaySnapshot>
)