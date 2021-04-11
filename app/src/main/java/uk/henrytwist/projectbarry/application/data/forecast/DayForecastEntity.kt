package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity

@Entity(primaryKeys = ["placeId", "time"])
class DayForecastEntity(
        val placeId: String,
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