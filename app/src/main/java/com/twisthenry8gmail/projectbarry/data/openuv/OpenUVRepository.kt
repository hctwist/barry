package com.twisthenry8gmail.projectbarry.data.openuv

import com.twisthenry8gmail.projectbarry.data.CachedData
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import javax.inject.Inject
import javax.inject.Singleton

@Deprecated("Replaced by individual call repositories like OneCallRepository")
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

            openUVLocalSource.getRealTimeUV(location.lat, location.lng)
        }, {

            openUVLocalSource.saveRealTimeUV(it)
        }, {

            openUVRemoteSource.getRealTimeUV(location.lat, location.lng)
        }, {

            location.isAt(location.lat, location.lng)
        })
    }
}