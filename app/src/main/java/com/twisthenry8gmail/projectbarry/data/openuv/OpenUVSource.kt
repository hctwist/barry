package com.twisthenry8gmail.projectbarry.data.openuv

import com.twisthenry8gmail.projectbarry.core.Result

interface OpenUVSource {

    suspend fun getRealTimeUV(lat: Double, lng: Double): Result<RealTimeData>

    class RealTimeData(
        val time: Long,
        val lat: Double,
        val lng: Double,
        val uv: Double
    )
}