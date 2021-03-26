package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["placeId", "time"])
class HourForecastEntity(
        val placeId: String,
        val time: Long,
        val temp: Double,
        val conditionCode: Int,
        val pop: Double
)