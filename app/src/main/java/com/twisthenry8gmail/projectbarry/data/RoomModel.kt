package com.twisthenry8gmail.projectbarry.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationEntity
import com.twisthenry8gmail.projectbarry.data.locations2.SavedLocationEntity

@TypeConverters(ForecastLocationEntity::class)
@Database(entities = [ForecastLocationEntity::class, SavedLocationEntity::class], version = 21)
abstract class RoomModel : RoomDatabase() {

    abstract fun forecastLocation2Dao(): SavedLocationEntity.Dao

    abstract fun forecastLocationDao(): ForecastLocationEntity.Dao
}