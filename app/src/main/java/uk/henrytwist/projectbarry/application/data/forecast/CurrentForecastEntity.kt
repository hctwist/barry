package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CurrentForecastEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val lat: Double,
        val lng: Double,
        val time: Long,
        val temp: Double,
        val conditionCode: Int,
        val feelsLike: Double,
        val uvIndex: Double,
        val dewPoint: Double,
        val windSpeed: Double,
)