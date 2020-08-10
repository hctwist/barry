package com.twisthenry8gmail.projectbarry.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationEntity

@TypeConverters(ForecastLocationEntity::class)
@Database(entities = [ForecastLocationEntity::class], version = 20)
abstract class RoomModel : RoomDatabase() {

    abstract fun forecastLocationDao(): ForecastLocationEntity.Dao
}