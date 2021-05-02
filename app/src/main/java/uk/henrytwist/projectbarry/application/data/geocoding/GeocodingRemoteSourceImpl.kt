package uk.henrytwist.projectbarry.application.data.geocoding

import android.location.Geocoder
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.henrytwist.kotlinbasics.outcomes.*
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource
import uk.henrytwist.projectbarry.domain.data.keys.APIKeyStore
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeocodingRemoteSourceImpl @Inject constructor(private val geocoder: Geocoder, private val volleyQueue: RequestQueue) : GeocodingRemoteSource {

    override suspend fun findLocation(
            placeId: String,
            autocompleteSessionToken: String?
    ): Outcome<Location> {

        val sessionTokenString =
                if (autocompleteSessionToken == null) "" else "&sessiontoken=$autocompleteSessionToken"

        val requestUrl = "https://maps.googleapis.com/maps/api/geocode/json?place_id=${placeId}&key=${APIKeyStore.getGeocodingKey()}$sessionTokenString"
        return suspendCoroutine { cont ->

            val request = JsonObjectRequest(
                    requestUrl,
                    null,
                    {

                        val results = it.getJSONArray("results")
                        val firstResult = results.getJSONObject(0)

                        val formattedAddress = firstResult.getString("formatted_address")

                        val locationGeometry =
                                firstResult.getJSONObject("geometry").getJSONObject("location")
                        val lat = locationGeometry.getDouble("lat")
                        val lng = locationGeometry.getDouble("lng")

                        cont.resume(Location(formattedAddress, LocationCoordinates(lat, lng)).asSuccess())
                    },
                    {

                        cont.resume(NetworkFailure())
                    }
            )

            volleyQueue.add(request)
        }
    }

    override suspend fun findLocation(coordinates: LocationCoordinates): Outcome<Location> {

        return findNameWithGeocoder(coordinates).switchFailure { findNameRemote(coordinates) }.map {

            Location(it, coordinates)
        }
    }

    private suspend fun findNameWithGeocoder(coordinates: LocationCoordinates): Outcome<String> {

        if (!Geocoder.isPresent()) return failure()

        return withContext(Dispatchers.IO) {

            try {

                val addresses = geocoder.getFromLocation(coordinates.lat, coordinates.lng, 10)

                addresses.forEach {

                    if (it.locality != null) return@withContext it.locality.asSuccess()
                }
                failure()
            } catch (exception: IOException) {

                failure()
            }
        }
    }

    private suspend fun findNameRemote(coordinates: LocationCoordinates): Outcome<String> {

        val requestUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=${coordinates.lat},${coordinates.lng}&result_type=locality&key=${APIKeyStore.getGeocodingKey()}"

        return suspendCoroutine { cont ->

            val request = JsonObjectRequest(
                    requestUrl,
                    null,
                    {

                        val results = it.getJSONArray("results")
                        val firstResult = results.getJSONObject(0)

                        val formattedAddress = firstResult.getString("formatted_address")

                        cont.resume(formattedAddress.asSuccess())
                    },
                    {

                        cont.resume(NetworkFailure())
                    }
            )

            volleyQueue.add(request)
        }
    }
}