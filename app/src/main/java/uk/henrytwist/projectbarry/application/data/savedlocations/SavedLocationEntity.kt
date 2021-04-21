package uk.henrytwist.projectbarry.application.data.savedlocations

import androidx.room.*

@Entity
class SavedLocationEntity(
        @PrimaryKey val placeId: String,
        val name: String,
        val lat: Double,
        val lng: Double,
        val pinned: Boolean
) {

    @androidx.room.Dao
    interface Dao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(location: SavedLocationEntity): Long

        @Query("SELECT * FROM SavedLocationEntity")
        suspend fun getAll(): List<SavedLocationEntity>

        @Query("SELECT * FROM SavedLocationEntity WHERE pinned")
        suspend fun getPinned(): List<SavedLocationEntity>

        @Query("UPDATE SavedLocationEntity SET pinned = :pinned WHERE placeId = :placeId")
        suspend fun updatePinned(placeId: String, pinned: Boolean)

        @Query("SELECT * FROM SavedLocationEntity WHERE placeId = :placeId")
        suspend fun get(placeId: String): SavedLocationEntity?
    }
}