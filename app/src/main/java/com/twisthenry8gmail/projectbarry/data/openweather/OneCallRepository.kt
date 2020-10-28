package com.twisthenry8gmail.projectbarry.data.openweather

import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.data.DataUtil
import com.twisthenry8gmail.projectbarry.data.OneShotForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class OneCallRepository @Inject constructor(
    private val openWeatherLocalSource: OpenWeatherLocalSource,
    private val openWeatherRemoteSource: OpenWeatherRemoteSource
) : OneShotForecastRepository<OpenWeatherSource.OneCallData>() {

    override suspend fun isStale(
        data: OpenWeatherSource.OneCallData,
        location: LocationData
    ): Boolean {

        val now = Instant.now()
        val time = Instant.ofEpochSecond(data.time)

        val locationClose = DataUtil.latLngClose(data.lat, data.lng, location.lat, location.lng)

        return !locationClose || time.until(now, ChronoUnit.MINUTES) > 10
    }

    override suspend fun saveLocal(data: OpenWeatherSource.OneCallData) {

        openWeatherLocalSource.saveOneCallData(data)
    }

    override suspend fun fetchLocal(location: LocationData): Result<OpenWeatherSource.OneCallData> {

        return openWeatherLocalSource.getOneCallData(location.lat, location.lng)
    }

    override suspend fun fetchRemote(location: LocationData): Result<OpenWeatherSource.OneCallData> {

        return openWeatherRemoteSource.getOneCallData(location.lat, location.lng)
    }
}