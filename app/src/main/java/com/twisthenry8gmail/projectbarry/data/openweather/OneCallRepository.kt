package com.twisthenry8gmail.projectbarry.data.openweather

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.StaticForecastRepository
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class OneCallRepository @Inject constructor(
    private val openWeatherLocalSource: OpenWeatherLocalSource,
    private val openWeatherRemoteSource: OpenWeatherRemoteSource
) : StaticForecastRepository<OpenWeatherSource.OneCallData>() {

    override suspend fun isStale(data: OpenWeatherSource.OneCallData): Boolean {

        val now = Instant.now()
        val time = Instant.ofEpochSecond(data.time)

        return time.until(now, ChronoUnit.MINUTES) > 10
    }

    override suspend fun saveLocal(data: OpenWeatherSource.OneCallData) {

        openWeatherLocalSource.saveOneCallData(data)
    }

    override suspend fun fetchLocal(location: ForecastLocation): Result<OpenWeatherSource.OneCallData> {

        return openWeatherLocalSource.getOneCallData(location.lat, location.lng)
    }

    override suspend fun fetchRemote(location: ForecastLocation): Result<OpenWeatherSource.OneCallData> {

        return openWeatherRemoteSource.getOneCallData(location.lat, location.lng)
    }
}