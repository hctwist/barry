package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.Result

interface GeocodingRemoteSource {

    suspend fun findLocationDetails(
        placeId: String,
        autocompleteSessionToken: String?
    ): Result<GeocodingDetails>

    suspend fun findLocationDetails(
        lat: Double,
        lng: Double
    ): Result<GeocodingDetails>

    class GeocodingDetails(val name: String, val lat: Double, val lng: Double)
}