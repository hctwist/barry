package com.twisthenry8gmail.projectbarry.data.locations

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.twisthenry8gmail.projectbarry.data.APIKeyStore
import com.twisthenry8gmail.projectbarry.data.Result
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ForecastLocationRemoteSource @Inject constructor(
    private val volleyQueue: RequestQueue,
    private val client: PlacesClient
) {

    suspend fun findLocations(query: String, sessionToken: AutocompleteSessionToken) =
        suspendCoroutine<List<AutocompletePrediction>> { continuation ->

            val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
                .setTypeFilter(TypeFilter.REGIONS).setSessionToken(sessionToken).build()
            client.findAutocompletePredictions(request).addOnSuccessListener {

                continuation.resume(it.autocompletePredictions)
            }
        }

    suspend fun getLocationDetails(
        lat: Double,
        lng: Double,
        sessionToken: AutocompleteSessionToken?
    ) =
        suspendCoroutine<Result<LocationDetails>> { cont ->

            val sessionTokenString = if (sessionToken == null) "" else "&sessiontoken=$sessionToken"

            val request = JsonObjectRequest(
                "https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&result_type=locality&key=${APIKeyStore.getGeocodingKey()}$sessionTokenString",
                null,
                {

                    val results = it.getJSONArray("results")
                    val firstResult = results.getJSONObject(0)

                    val placeId = firstResult.getString("place_id")
                    val formattedAddress = firstResult.getString("formatted_address")

                    cont.resume(
                        Result.Success(
                            LocationDetails(
                                placeId,
                                formattedAddress,
                                lat,
                                lng
                            )
                        )
                    )
                },
                {

                    cont.resume(Result.Failure())
                })

            volleyQueue.add(request)
        }

    suspend fun getLocationDetails(placeId: String, sessionToken: AutocompleteSessionToken?) =
        suspendCoroutine<Result<LocationDetails>> { cont ->

            val sessionTokenString = if (sessionToken == null) "" else "&sessiontoken=$sessionToken"

            val request = JsonObjectRequest(
                "https://maps.googleapis.com/maps/api/geocode/json?place_id=${placeId}&key=${APIKeyStore.getGeocodingKey()}$sessionTokenString",
                null,
                {

                    val results = it.getJSONArray("results")
                    val firstResult = results.getJSONObject(0)

                    val formattedAddress = firstResult.getString("formatted_address")

                    val locationGeometry =
                        firstResult.getJSONObject("geometry").getJSONObject("location")
                    val lat = locationGeometry.getDouble("lat")
                    val lng = locationGeometry.getDouble("lng")

                    cont.resume(
                        Result.Success(
                            LocationDetails(
                                placeId,
                                formattedAddress,
                                lat,
                                lng
                            )
                        )
                    )
                },
                {

                    cont.resume(Result.Failure())
                }
            )

            volleyQueue.add(request)
        }

    class LocationDetails(val placeId: String, val name: String, val lat: Double, val lng: Double)
}