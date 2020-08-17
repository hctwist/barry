package com.twisthenry8gmail.projectbarry.data.openweather

import com.twisthenry8gmail.projectbarry.data.CachedData
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenWeatherRepository @Inject constructor(
    private val openWeatherLocalSource: OpenWeatherLocalSource,
    private val openWeatherRemoteSource: OpenWeatherRemoteSource
) {

    private var oneCallCache = CachedData<OpenWeatherSource.OneCallData>()

    suspend fun getOneCallData(
        location: ForecastLocation,
        forceRefresh: Boolean
    ): Result<OpenWeatherSource.OneCallData> {

        if (forceRefresh) oneCallCache.invalidate()

        return oneCallCache.get({

            openWeatherLocalSource.getOneCallData(location.lat, location.lng)
        }, {

            openWeatherLocalSource.saveOneCallData(it)
        }, {

            openWeatherRemoteSource.getOneCallData(location.lat, location.lng)
        }, {

            location.isAt(it.lat, it.lng)
        })
    }
}