package com.twisthenry8gmail.projectbarry.data.locations

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
class SavedLocationEntity(
    val name: String,
    val lat: Double,
    val lng: Double,
    val pinned: Boolean
) {

    // TODO Long for this?
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @androidx.room.Dao
    interface Dao {

        @Insert
        suspend fun insert(location: SavedLocationEntity): Long

        @Query("SELECT * FROM SavedLocationEntity")
        suspend fun getAll(): List<SavedLocationEntity>

        @Query("SELECT * FROM SavedLocationEntity WHERE pinned")
        suspend fun getPinned(): List<SavedLocationEntity>

        @Query("UPDATE SavedLocationEntity SET pinned = :pinned WHERE id = :id")
        suspend fun updatePinned(id: Int, pinned: Boolean)

        @Query("SELECT * FROM SavedLocationEntity WHERE id = :id LIMIT 1")
        suspend fun get(id: Int): SavedLocationEntity?
    }
}