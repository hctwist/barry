package uk.henrytwist.projectbarry.application.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.henrytwist.projectbarry.application.data.forecast.CurrentForecastEntity
import uk.henrytwist.projectbarry.application.data.forecast.DayForecastEntity
import uk.henrytwist.projectbarry.application.data.forecast.ForecastDao
import uk.henrytwist.projectbarry.application.data.forecast.HourForecastEntity
import uk.henrytwist.projectbarry.application.data.savedlocations.SavedLocationEntity

@Database(entities = [SavedLocationEntity::class, CurrentForecastEntity::class, HourForecastEntity::class, DayForecastEntity::class], version = 27)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedLocationDao(): SavedLocationEntity.Dao

    abstract fun forecastDao(): ForecastDao

    companion object {

        // TODO Migrations
        fun build(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "barry_db")
                .fallbackToDestructiveMigration().build()
    }
}