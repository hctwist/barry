package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["placeId", "time"])
class DayForecastEntity(
        val placeId: String,
        val time: Long,
        val tempLow: Double,
        val tempHigh: Double,
        val conditionCode: Int,
        val pop: Double,
        val sunrise: Long,
        val sunset: Long
)