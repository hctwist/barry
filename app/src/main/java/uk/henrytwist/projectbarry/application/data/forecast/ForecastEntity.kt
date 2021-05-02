package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.Embedded
import androidx.room.Relation

class ForecastEntity(
        @Embedded
        val currentForecastEntity: CurrentForecastEntity,
        @Relation(parentColumn = "id", entityColumn = "id")
        val hourForecastEntities: List<HourForecastEntity>,
        @Relation(parentColumn = "id", entityColumn = "id")
        val dayForecastEntities: List<DayForecastEntity>,
)