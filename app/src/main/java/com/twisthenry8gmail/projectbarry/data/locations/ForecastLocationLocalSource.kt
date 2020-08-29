package com.twisthenry8gmail.projectbarry.data.locations

import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import javax.inject.Inject

class ForecastLocationLocalSource @Inject constructor(private val forecastLocationDao: ForecastLocationEntity.Dao) {

    suspend fun addLocation(forecastLocation: ForecastLocation) {

        forecastLocationDao.insert(forecastLocation.toEntity())
    }

    suspend fun removeLocation(placeId: String) {

        forecastLocationDao.delete(placeId)
    }

    suspend fun getLocation(placeId: String): ForecastLocation? {

        return forecastLocationDao.get(placeId)?.toForecastLocation()
    }

    suspend fun getLocationType(placeId: String): ForecastLocation.Type? {

        return forecastLocationDao.getType(placeId)
    }

    suspend fun getLocationsOf(types: List<ForecastLocation.Type>): List<ForecastLocation> {

        return forecastLocationDao.getFrom(types).map { it.toForecastLocation() }
    }

    suspend fun removeAllOfType(type: ForecastLocation.Type) {

        forecastLocationDao.deleteAllOfType(type)
    }

    suspend fun replaceAllOfType(location: ForecastLocation) {

        forecastLocationDao.replaceAllOfType(location.toEntity())
    }

    suspend fun pin(placeId: String) {

        forecastLocationDao.setType(placeId, ForecastLocation.Type.PINNED)
    }

    private fun ForecastLocation.toEntity() = ForecastLocationEntity(
        placeId,
        name,
        lat,
        lng,
        type
    )

    private fun ForecastLocationEntity.toForecastLocation() = ForecastLocation(
        placeId,
        name,
        lat,
        lng,
        type
    )
}