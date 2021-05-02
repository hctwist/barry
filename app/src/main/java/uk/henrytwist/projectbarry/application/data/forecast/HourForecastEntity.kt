package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity

@Entity(primaryKeys = ["id", "time"])
class HourForecastEntity(
        var id: Int,
        val time: Long,
        val temp: Double,
        val conditionCode: Int,
        val uvIndex: Double,
        val windSpeed: Double,
        val pop: Double
)