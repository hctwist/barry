package uk.henrytwist.projectbarry.application.data.forecast

import androidx.room.*

@Dao
interface ForecastDao {

    @Query("SELECT id, lat, lng FROM CurrentForecastEntity WHERE time > :afterTime")
    suspend fun getCoordinates(afterTime: Long): List<Coordinates>

    @Transaction
    @Query("SELECT * FROM CurrentForecastEntity WHERE id = :id")
    suspend fun get(id: Int): ForecastEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentForecast(entity: CurrentForecastEntity): Long

    @Insert
    suspend fun insertHourForecasts(entities: List<HourForecastEntity>)

    @Insert
    suspend fun insertDayForecasts(entities: List<DayForecastEntity>)

    @Query("SELECT id FROM CurrentForecastEntity WHERE time < :beforeTime")
    suspend fun findStaleForecasts(beforeTime: Long): List<Int>

    @Query("DELETE FROM CurrentForecastEntity WHERE id = :id")
    suspend fun deleteCurrentForecast(id: Int)

    @Query("DELETE FROM HourForecastEntity WHERE id = :id")
    suspend fun deleteHourForecasts(id: Int)

    @Query("DELETE FROM DayForecastEntity WHERE id = :id")
    suspend fun deleteDayForecasts(id: Int)

    @Transaction
    suspend fun insert(entity: ForecastEntity) {

        val id = insertCurrentForecast(entity.currentForecastEntity).toInt()

        entity.hourForecastEntities.forEach { it.id = id }
        entity.dayForecastEntities.forEach { it.id = id }

        insertHourForecasts(entity.hourForecastEntities)
        insertDayForecasts(entity.dayForecastEntities)
    }

    @Transaction
    suspend fun deleteForecasts(beforeTime: Long) {

        val ids = findStaleForecasts(beforeTime)
        ids.forEach {

            deleteCurrentForecast(it)
            deleteHourForecasts(it)
            deleteDayForecasts(it)
        }
    }

    class Coordinates(val id: Int, val lat: Double, val lng: Double)
}