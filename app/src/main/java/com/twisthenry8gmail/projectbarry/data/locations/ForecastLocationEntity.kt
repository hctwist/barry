package com.twisthenry8gmail.projectbarry.data.locations

import androidx.room.*

@Entity
class ForecastLocationEntity(
    @PrimaryKey val placeId: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    @field:TypeConverters(ForecastLocationEntity::class) val type: ForecastLocation.Type
) {

    companion object {

        @JvmStatic
        @TypeConverter
        fun fromType(type: ForecastLocation.Type) = type.ordinal

        @JvmStatic
        @TypeConverter
        fun toType(type: Int) = ForecastLocation.Type.values()[type]
    }

    @androidx.room.Dao
    interface Dao {

        @Query("SELECT * FROM ForecastLocationEntity WHERE placeId = :placeId")
        suspend fun get(placeId: String): ForecastLocationEntity?

        @Query("SELECT type FROM ForecastLocationEntity WHERE placeId = :placeId")
        suspend fun getType(placeId: String): ForecastLocation.Type?

        @Query("SELECT * FROM ForecastLocationEntity WHERE type IN (:types)")
        suspend fun getFrom(types: List<ForecastLocation.Type>): List<ForecastLocationEntity>

        @Query("DELETE FROM ForecastLocationEntity WHERE type = :type")
        suspend fun deleteAllOfType(type: ForecastLocation.Type)

        @Query("DELETE FROM ForecastLocationEntity WHERE placeId = :placeId")
        suspend fun delete(placeId: String)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(forecastLocationEntity: ForecastLocationEntity)

        @Query("UPDATE ForecastLocationEntity SET type = :type WHERE placeId = :placeId")
        suspend fun setType(placeId: String, type: ForecastLocation.Type)

        @Transaction
        suspend fun replaceAllOfType(forecastLocationEntity: ForecastLocationEntity) {

            deleteAllOfType(forecastLocationEntity.type)
            insert(forecastLocationEntity)
        }
    }
}