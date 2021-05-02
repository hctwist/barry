package uk.henrytwist.projectbarry.application.data.forecast

import uk.henrytwist.projectbarry.domain.models.LocationCoordinates

class ForecastModel(
        val coordinates: LocationCoordinates,
        val time: Long,
        val temp: Double,
        val conditionCode: Int,
        val feelsLike: Double,
        val uvIndex: Double,
        val dewPoint: Double,
        val windSpeed: Double,
        val hourly: List<Hour>,
        val daily: List<Day>
) {

    class Hour(val time: Long, val temp: Double, val conditionCode: Int, val uvIndex: Double, val windSpeed: Double, val pop: Double)

    class Day(
            val time: Long,
            val tempLow: Double,
            val tempHigh: Double,
            val conditionCode: Int,
            val uvIndex: Double,
            val pop: Double,
            val windSpeed: Double,
            val sunrise: Long,
            val sunset: Long
    )
}