package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity

@Entity(primaryKeys = ["placeId", "time"])
class HourForecastEntity(
        val placeId: String,
        val time: Long,
        val temp: Double,
        val conditionCode: Int,
        val uvIndex: Double,
        val windSpeed: Double,
        val pop: Double
)