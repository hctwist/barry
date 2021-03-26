package uk.henrytwist.projectbarry.domain.data.locationsearch

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource

class LocationSearchRepository<SessionToken>(
        private val searchRemoteSource: LocationSearchRemoteSource<SessionToken>,
        private val geocodingRemoteSource: GeocodingRemoteSource
) {

    private var sessionToken: SessionToken? = null

    suspend fun autocomplete(query: String): Outcome<List<LocationSearchResult>> {

        val newSessionToken = sessionToken ?: searchRemoteSource.newSessionToken()
        sessionToken = newSessionToken

        return searchRemoteSource.autocomplete(query, newSessionToken)
    }

    suspend fun getLocation(locationSearchResult: LocationSearchResult): Outcome<Location> {

        return geocodingRemoteSource.findLocation(
                locationSearchResult.placeId,
                sessionToken?.toString()
        )
    }
}