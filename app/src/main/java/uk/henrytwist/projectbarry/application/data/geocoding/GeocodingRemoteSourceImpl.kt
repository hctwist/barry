package uk.henrytwist.projectbarry.application.data.geocoding

import android.location.Geocoder
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import uk.henrytwist.kotlinbasics.outcomes.NetworkFailure
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource
import uk.henrytwist.projectbarry.domain.data.keys.APIKeyStore
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeocodingRemoteSourceImpl @Inject constructor(private val volleyQueue: RequestQueue) : GeocodingRemoteSource {

    override suspend fun findLocation(
            placeId: String,
            autocompleteSessionToken: String?
    ): Outcome<Location> {

        val sessionTokenString =
                if (autocompleteSessionToken == null) "" else "&sessiontoken=$autocompleteSessionToken"

        return findLocationDetailsInternal("https://maps.googleapis.com/maps/api/geocode/json?place_id=${placeId}&key=${APIKeyStore.getGeocodingKey()}$sessionTokenString")
    }

    override suspend fun findLocation(coordinates: LocationCoordinates): Outcome<Location> {

        return findLocationDetailsInternal("https://maps.googleapis.com/maps/api/geocode/json?latlng=${coordinates.lat},${coordinates.lng}&result_type=locality&key=${APIKeyStore.getGeocodingKey()}")
    }

    private suspend fun findLocationDetailsInternal(requestUrl: String): Outcome<Location> {

        return suspendCoroutine { cont ->

            val request = JsonObjectRequest(
                    requestUrl,
                    null,
                    {

                        val results = it.getJSONArray("results")
                        val firstResult = results.getJSONObject(0)

                        val placeId = firstResult.getString("place_id")

                        val formattedAddress = firstResult.getString("formatted_address")

                        val locationGeometry =
                                firstResult.getJSONObject("geometry").getJSONObject("location")
                        val lat = locationGeometry.getDouble("lat")
                        val lng = locationGeometry.getDouble("lng")

                        cont.resume(Location(placeId, formattedAddress, LocationCoordinates(lat, lng)).asSuccess())
                    },
                    {

                        cont.resume(NetworkFailure())
                    }
            )

            volleyQueue.add(request)
        }
    }
}