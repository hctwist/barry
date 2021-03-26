package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Embedded
import androidx.room.Relation

class ForecastEntity(
        @Embedded
        val currentForecastEntity: CurrentForecastEntity,
        @Relation(parentColumn = "placeId", entityColumn = "placeId")
        val hourForecastEntities: List<HourForecastEntity>,
        @Relation(parentColumn = "placeId", entityColumn = "placeId")
        val dayForecastEntities: List<DayForecastEntity>,
)