package com.twisthenry8gmail.projectbarry.data.openuv

import com.twisthenry8gmail.projectbarry.data.CachedData
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenUVRepository @Inject constructor(
    private val openUVRemoteSource: OpenUVRemoteSource,
    private val openUVLocalSource: OpenUVLocalSource
) {

    private var realTimeUVCache = CachedData<OpenUVSource.RealTimeData>()

    suspend fun getRealTimeUVData(
        location: ForecastLocation,
        forceRefresh: Boolean
    ): Result<OpenUVSource.RealTimeData> {

        if (forceRefresh) realTimeUVCache.invalidate()

        return realTimeUVCache.get({

            openUVLocalSource.getCurrentUV(location.lat, location.lng)
        }, {

            openUVLocalSource.saveCurrentUV(it)
        }, {

            openUVRemoteSource.getCurrentUV(location.lat, location.lng)
        }, {

            location.isAt(location.lat, location.lng)
        })
    }
}