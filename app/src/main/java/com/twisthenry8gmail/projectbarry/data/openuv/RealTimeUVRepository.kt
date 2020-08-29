package com.twisthenry8gmail.projectbarry.data.openuv

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.StaticForecastRepository
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RealTimeUVRepository @Inject constructor(
    private val openUVRemoteSource: OpenUVRemoteSource,
    private val openUVLocalSource: OpenUVLocalSource
) : StaticForecastRepository<OpenUVSource.RealTimeData>() {

    override suspend fun isStale(data: OpenUVSource.RealTimeData): Boolean {

        val now = Instant.now()
        val time = Instant.ofEpochSecond(data.time)

        return time.until(now, ChronoUnit.MINUTES) > 10
    }

    override suspend fun saveLocal(data: OpenUVSource.RealTimeData) {

        openUVLocalSource.saveRealTimeUV(data)
    }

    override suspend fun fetchLocal(location: ForecastLocation): Result<OpenUVSource.RealTimeData> {

        return openUVLocalSource.getRealTimeUV(location.lat, location.lng)
    }

    override suspend fun fetchRemote(location: ForecastLocation): Result<OpenUVSource.RealTimeData> {

        return openUVRemoteSource.getRealTimeUV(location.lat, location.lng)
    }


}