package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

class NowForecast(
        val condition: WeatherCondition,
        val conditionChange: ConditionChange,
        val isNight: Boolean,
        val temp: ScaledTemperature,
        val feelsLike: ScaledTemperature,
        val elements: List<ForecastElement>,
        val daySnapshot: DaySnapshot,
        val hourSnapshots: List<HourSnapshot>
) {

    class DaySnapshot(val isToday: Boolean, val condition: WeatherCondition, val tempLow: ScaledTemperature, val tempHigh: ScaledTemperature, val sunrise: ZonedDateTime, val sunset: ZonedDateTime)

    class HourSnapshot(val time: ZonedDateTime, val condition: WeatherCondition, val isNight: Boolean)
}