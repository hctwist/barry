package uk.henrytwist.projectbarry.domain.data.geocoding

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates

interface GeocodingRemoteSource {

    suspend fun findLocation(placeId: String, autocompleteSessionToken: String?): Outcome<Location>

    suspend fun findLocation(coordinates: LocationCoordinates): Outcome<Location>
}