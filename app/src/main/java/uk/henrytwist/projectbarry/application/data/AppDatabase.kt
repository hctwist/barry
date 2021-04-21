package uk.henrytwist.projectbarry.application.data

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.henrytwist.projectbarry.application.data.forecast.CurrentForecastEntity
import uk.henrytwist.projectbarry.application.data.forecast.DayForecastEntity
import uk.henrytwist.projectbarry.application.data.forecast.ForecastDao
import uk.henrytwist.projectbarry.application.data.forecast.HourForecastEntity
import uk.henrytwist.projectbarry.application.data.savedlocations.SavedLocationEntity
import uk.henrytwist.projectbarry.application.data.uv.UVDao
import uk.henrytwist.projectbarry.application.data.uv.UVEntity

@Database(entities = [SavedLocationEntity::class, CurrentForecastEntity::class, HourForecastEntity::class, DayForecastEntity::class, UVEntity::class], version = 25)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedLocationDao(): SavedLocationEntity.Dao

    abstract fun forecastDao(): ForecastDao

    abstract fun uvDao(): UVDao
}