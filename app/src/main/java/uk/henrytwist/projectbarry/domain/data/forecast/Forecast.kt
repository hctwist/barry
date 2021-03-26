package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import java.time.Instant

class Forecast(
        val placeId: String,
        val time: Instant,
        val temp: ScaledTemperature,
        val condition: WeatherCondition,
        val feelsLike: ScaledTemperature,
        val humidity: Int,
        val windSpeed: Double,
        val hourly: List<Hour>,
        val daily: List<Day>
) {

    class Hour(val time: Instant, val temp: ScaledTemperature, val condition: WeatherCondition, val pop: Double)

    class Day(
            val time: Instant,
            val tempLow: ScaledTemperature,
            val tempHigh: ScaledTemperature,
            val condition: WeatherCondition,
            val pop: Double,
            val sunrise: Instant,
            val sunset: Instant
    )
}