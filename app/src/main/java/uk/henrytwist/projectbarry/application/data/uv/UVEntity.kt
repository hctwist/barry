package uk.henrytwist.projectbarry.application.data.uv

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UVEntity(
        @PrimaryKey val placeId: String,
        val time: Long,
        val uv: Double
)