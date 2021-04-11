package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CurrentForecastEntity(
        @PrimaryKey
        val placeId: String,
        val time: Long,
        val temp: Double,
        val conditionCode: Int,
        val feelsLike: Double,
        val uvIndex: Double,
        val humidity: Int,
        val windSpeed: Double,
)