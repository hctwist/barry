package com.twisthenry8gmail.projectbarry.application.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.twisthenry8gmail.projectbarry.application.data.locations.SavedLocationEntity

@Database(entities = [SavedLocationEntity::class], version = 22)
abstract class RoomModel : RoomDatabase() {

    abstract fun forecastLocation2Dao(): SavedLocationEntity.Dao
}