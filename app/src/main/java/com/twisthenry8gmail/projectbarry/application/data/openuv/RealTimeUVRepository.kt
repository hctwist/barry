package com.twisthenry8gmail.projectbarry.application.data.openuv

import com.twisthenry8gmail.projectbarry.domain.core.LocationData
import com.twisthenry8gmail.projectbarry.domain.core.Result
import com.twisthenry8gmail.projectbarry.application.data.DataUtil
import com.twisthenry8gmail.projectbarry.application.data.OneShotForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RealTimeUVRepository @Inject constructor(
    private val openUVRemoteSource: OpenUVRemoteSource,
    private val openUVLocalSource: OpenUVLocalSource
) : OneShotForecastRepository<OpenUVSource.RealTimeData>() {

    override suspend fun isStale(
        data: OpenUVSource.RealTimeData,
        location: LocationData
    ): Boolean {

        val now = Instant.now()
        val time = Instant.ofEpochSecond(data.time)

        val locationClose = DataUtil.latLngClose(data.lat, data.lng, location.lat, location.lng)

        return !locationClose || time.until(now, ChronoUnit.MINUTES) > 10
    }

    override suspend fun saveLocal(data: OpenUVSource.RealTimeData) {

        openUVLocalSource.saveRealTimeUV(data)
    }

    override suspend fun fetchLocal(location: LocationData): Result<OpenUVSource.RealTimeData> {

        return openUVLocalSource.getRealTimeUV(location.lat, location.lng)
    }

    override suspend fun fetchRemote(location: LocationData): Result<OpenUVSource.RealTimeData> {

        return openUVRemoteSource.getRealTimeUV(location.lat, location.lng)
    }


}