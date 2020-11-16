package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.LocationData
import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.Result

class LocationAutocompleteRepository<SessionToken>(
    private val autocompleteRemoteSource: LocationAutocompleteRemoteSource<SessionToken>,
    private val geocodingRemoteSource: GeocodingRemoteSource
) {

    private var sessionToken: SessionToken? = null

    suspend fun autocomplete(query: String): Result<List<LocationSearchResult>> {

        val newSessionToken = sessionToken ?: autocompleteRemoteSource.newSessionToken()
        sessionToken = newSessionToken

        return autocompleteRemoteSource.autocomplete(query, newSessionToken)
    }

    suspend fun getLocation(locationSearchResult: LocationSearchResult): Result<LocationData> {

        return geocodingRemoteSource.findLocationDetails(
            locationSearchResult.placeId,
            sessionToken?.toString()
        ).map {

            LocationData(it.name, it.lat, it.lng)
        }
    }
}