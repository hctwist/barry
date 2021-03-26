package uk.henrytwist.projectbarry.application.data.uv

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UVDao {

    @Query("SELECT * FROM UVEntity WHERE placeId = :placeId")
    suspend fun get(placeId: String): UVEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(uvEntity: UVEntity)
}