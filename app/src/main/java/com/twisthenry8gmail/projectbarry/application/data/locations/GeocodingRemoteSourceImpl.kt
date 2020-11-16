package com.twisthenry8gmail.projectbarry.application.data.locations

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.twisthenry8gmail.projectbarry.domain.core.Result
import com.twisthenry8gmail.projectbarry.domain.core.failure
import com.twisthenry8gmail.projectbarry.domain.core.success
import com.twisthenry8gmail.projectbarry.application.data.APIKeyStore
import com.twisthenry8gmail.projectbarry.domain.data.locations.GeocodingRemoteSource
import com.twisthenry8gmail.projectbarry.domain.data.locations.GeocodingRemoteSource.GeocodingDetails
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeocodingRemoteSourceImpl @Inject constructor(private val volleyQueue: RequestQueue) : GeocodingRemoteSource {

    override suspend fun findLocationDetails(
        placeId: String,
        autocompleteSessionToken: String?
    ): Result<GeocodingDetails> {

        val sessionTokenString =
            if (autocompleteSessionToken == null) "" else "&sessiontoken=$autocompleteSessionToken"

        return findLocationDetailsInternal("https://maps.googleapis.com/maps/api/geocode/json?place_id=${placeId}&key=${APIKeyStore.getGeocodingKey()}$sessionTokenString")
    }

    override suspend fun findLocationDetails(lat: Double, lng: Double): Result<GeocodingDetails> {

        return findLocationDetailsInternal("https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&result_type=locality&key=${APIKeyStore.getGeocodingKey()}")
    }

    private suspend fun findLocationDetailsInternal(requestUrl: String): Result<GeocodingDetails> {

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

                    cont.resume(success(GeocodingDetails(formattedAddress, lat, lng)))
                },
                {

                    cont.resume(failure())
                }
            )

            volleyQueue.add(request)
        }
    }
}