package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.LocationData
import com.twisthenry8gmail.projectbarry.domain.core.Result

class GeocodingRepository(private val geocodingRemoteSource: GeocodingRemoteSource) {

    // TODO Needed? And input should not be placeId but some POJO. Wait to see what it is used for
    suspend fun findLocationDetails(
        placeId: String
    ): Result<LocationData> {

        return geocodingRemoteSource.findLocationDetails(placeId, null).map {

            it.toLocationData()
        }
    }

    suspend fun findLocationDetails(
        lat: Double,
        lng: Double
    ): Result<LocationData> {

        return geocodingRemoteSource.findLocationDetails(lat, lng).map {

            it.toLocationData()
        }
    }

    private fun GeocodingRemoteSource.GeocodingDetails.toLocationData() =
        LocationData(name, lat, lng)
}