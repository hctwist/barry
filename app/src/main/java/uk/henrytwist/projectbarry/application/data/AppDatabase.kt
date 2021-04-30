package uk.henrytwist.projectbarry.application.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uk.henrytwist.projectbarry.application.data.forecast.CurrentForecastEntity
import uk.henrytwist.projectbarry.application.data.forecast.DayForecastEntity
import uk.henrytwist.projectbarry.application.data.forecast.ForecastDao
import uk.henrytwist.projectbarry.application.data.forecast.HourForecastEntity
import uk.henrytwist.projectbarry.application.data.savedlocations.SavedLocationEntity

@Database(entities = [SavedLocationEntity::class, CurrentForecastEntity::class, HourForecastEntity::class, DayForecastEntity::class], version = 26)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedLocationDao(): SavedLocationEntity.Dao

    abstract fun forecastDao(): ForecastDao

    companion object {

        fun build(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "barry_db")
                .addMigrations(
                        MIGRATION_25_26
                ).build()

        // TODO Remove and reset version on first production build
        private val MIGRATION_25_26 = object : Migration(25, 26) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("DROP TABLE IF EXISTS UVEntity")
            }
        }
    }
}