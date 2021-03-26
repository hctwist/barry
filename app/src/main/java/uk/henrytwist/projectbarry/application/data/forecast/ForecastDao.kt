package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.*

@Dao
interface ForecastDao {

    @Transaction
    @Query("SELECT * FROM CurrentForecastEntity WHERE placeId = :placeId")
    suspend fun get(placeId: String): ForecastEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentForecast(entity: CurrentForecastEntity)

    @Query("DELETE FROM HourForecastEntity WHERE placeId = :placeId")
    suspend fun deleteHourForecastsFor(placeId: String)

    @Insert
    suspend fun insertHourForecasts(entities: List<HourForecastEntity>)

    @Query("DELETE FROM DayForecastEntity WHERE placeId = :placeId")
    suspend fun deleteDayForecastsFor(placeId: String)

    @Insert
    suspend fun insertDayForecasts(entities: List<DayForecastEntity>)

    @Transaction
    suspend fun insert(entity: ForecastEntity) {

        insertCurrentForecast(entity.currentForecastEntity)
        deleteHourForecastsFor(entity.currentForecastEntity.placeId)
        insertHourForecasts(entity.hourForecastEntities)
        deleteDayForecastsFor(entity.currentForecastEntity.placeId)
        insertDayForecasts(entity.dayForecastEntities)
    }
}