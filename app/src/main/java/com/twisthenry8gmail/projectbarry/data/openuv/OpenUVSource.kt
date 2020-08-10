package com.twisthenry8gmail.projectbarry.data.openuv

import com.twisthenry8gmail.projectbarry.data.Result

interface OpenUVSource {

    suspend fun getCurrentUV(lat: Double, lng: Double): Result<RealTimeData>

    class RealTimeData(
        val time: Long,
        val lat: Double,
        val lng: Double,
        val uv: Double
    )
}