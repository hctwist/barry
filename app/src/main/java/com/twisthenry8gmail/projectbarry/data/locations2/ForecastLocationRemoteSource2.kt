package com.twisthenry8gmail.projectbarry.data.locations2

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.failure
import com.twisthenry8gmail.projectbarry.core.success
import com.twisthenry8gmail.projectbarry.data.APIKeyStore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ForecastLocationRemoteSource2 @Inject constructor(
    private val placesClient: PlacesClient,
    private val volleyQueue: RequestQueue
) {

    private var autocompleteSessionToken: AutocompleteSessionToken? = null

    suspend fun search(query: String): Result<List<AutocompletePrediction>> {

        val sessionToken = autocompleteSessionToken ?: AutocompleteSessionToken.newInstance()
        autocompleteSessionToken = sessionToken

        return suspendCoroutine { cont ->

            val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
                .setTypeFilter(TypeFilter.REGIONS).setSessionToken(sessionToken).build()
            placesClient.findAutocompletePredictions(request).addOnCompleteListener {

                val result = it.result
                if (it.isSuccessful && result != null) {

                    cont.resume(success(result.autocompletePredictions))
                } else {

                    cont.resume(failure())
                }
            }
        }
    }

    suspend fun findLocationDetails(placeId: String): Result<GeocodingDetails> {

        val sessionTokenString =
            if (autocompleteSessionToken == null) "" else "&sessiontoken=$autocompleteSessionToken"

        return findLocationDetailsInternal("https://maps.googleapis.com/maps/api/geocode/json?place_id=${placeId}&key=${APIKeyStore.getGeocodingKey()}$sessionTokenString")
    }

    suspend fun findLocationDetails(lat: Double, lng: Double): Result<GeocodingDetails> {

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

    class GeocodingDetails(val name: String, val lat: Double, val lng: Double)
}