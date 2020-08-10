package com.twisthenry8gmail.projectbarry.data.locations

import android.content.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.twisthenry8gmail.projectbarry.data.Result
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import com.twisthenry8gmail.projectbarry.data.failure
import com.twisthenry8gmail.projectbarry.data.success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class ForecastLocationRepository @Inject constructor(
    private val forecastLocationLocalSource: ForecastLocationLocalSource,
    private val forecastLocationRemoteSource: ForecastLocationRemoteSource,
    private val locationClient: FusedLocationProviderClient,
    @SharedPreferencesModule.Data private val dataPrefs: SharedPreferences
) {

    private var savedLocationsJob: Deferred<List<ForecastLocation>>? = null

    private var autocompleteSessionToken: AutocompleteSessionToken? = null

    private val selectedPlaceListeners = mutableListOf<(String?) -> Unit>()

    fun registerSelectedPlaceListener(listener: (String?) -> Unit) {

        selectedPlaceListeners.add(listener)
    }

    fun deregisterSelectedPlaceListener(listener: (String?) -> Unit) {

        selectedPlaceListeners.remove(listener)
    }

    fun getSelectedPlaceId(): String? {

        return dataPrefs.getString(KEY_SELECTED_LOCATION, null)
    }

    suspend fun getSelectedLocation(): Result<ForecastLocation> {

        val selectedId = getSelectedPlaceId()

        if (selectedId == null) {

            val currentLocation = getCurrentLocation()

            return if (currentLocation is Result.Success) {

                replaceAllOfType(currentLocation.data)
                currentLocation
            } else {

                failure()
            }
        } else {

            val selectedLocation = forecastLocationLocalSource.getLocation(selectedId)
            return if (selectedLocation == null) failure() else success(selectedLocation)
        }
    }

    fun removeSelection() {

        dataPrefs.edit().remove(KEY_SELECTED_LOCATION).apply()
        onSelectedPlaceChanged(null)
    }

    fun select(placeId: String) {

        dataPrefs.edit().putString(KEY_SELECTED_LOCATION, placeId).apply()
        onSelectedPlaceChanged(placeId)
    }

    private fun onSelectedPlaceChanged(placeId: String?) {

        selectedPlaceListeners.forEach { it(placeId) }
    }

    suspend fun pin(placeId: String) {

        forecastLocationLocalSource.pin(placeId)
    }

    suspend fun getCurrentLocation(): Result<ForecastLocation> {

        val latLng = suspendCoroutine<Result<Pair<Double, Double>>> { cont ->

            try {
                locationClient.lastLocation.addOnCompleteListener {

                    val result = it.result
                    if (it.isSuccessful && result != null) {

                        cont.resume(Result.Success(result.latitude to result.longitude))
                    } else {

                        cont.resume(Result.Failure())
                    }
                }
            } catch (securityException: SecurityException) {

                cont.resume(Result.Failure())
            }
        }

        if (latLng is Result.Success) {

            val lat = latLng.data.first
            val lng = latLng.data.second

            val locationDetails =
                forecastLocationRemoteSource.getLocationDetails(lat, lng, autocompleteSessionToken)

            return locationDetails.map {

                ForecastLocation(
                    it.placeId,
                    it.name,
                    it.lat,
                    it.lng,
                    ForecastLocation.Type.CURRENT_LOCATION
                )
            }
        }

        return Result.Failure()
    }

    suspend fun getLocation(
        placeId: String
    ): Result<ForecastLocation> {

        val locationDetails =
            forecastLocationRemoteSource.getLocationDetails(placeId, autocompleteSessionToken)

        return locationDetails.map {

            ForecastLocation(it.placeId, it.name, it.lat, it.lng, ForecastLocation.Type.CHOSEN)
        }
    }

    suspend fun addLocation(forecastLocation: ForecastLocation) {

        forecastLocationLocalSource.addLocation(forecastLocation)
    }

    suspend fun removeLocation(placeId: String) {

        forecastLocationLocalSource.removeLocation(placeId)
    }

    suspend fun removeAllOfType(type: ForecastLocation.Type) {

        forecastLocationLocalSource.removeAllOfType(type)
    }

    suspend fun replaceAllOfType(location: ForecastLocation) {

        forecastLocationLocalSource.replaceAllOfType(location)
    }

    suspend fun getPinnedAndChosenPlaces(): List<ForecastLocation> {

        coroutineScope {

            savedLocationsJob = async {
                forecastLocationLocalSource.getLocationsOf(
                    listOf(
                        ForecastLocation.Type.CHOSEN,
                        ForecastLocation.Type.PINNED
                    )
                )
            }
        }

        return savedLocationsJob!!.await()
    }

    suspend fun findPlaces(query: String): List<LocationSearchResult> {

        val token = autocompleteSessionToken ?: AutocompleteSessionToken.newInstance()
        autocompleteSessionToken = token

        val autocompletePlaces = forecastLocationRemoteSource.findLocations(query, token)
        return autocompletePlaces.map { prediction ->

            LocationSearchResult(
                prediction.placeId,
                prediction.getPrimaryText(null).toString(),
                prediction.getSecondaryText(null).toString()
            )
        }
    }

    fun onAutocompleteSessionFinished() {

        autocompleteSessionToken = null
    }

    companion object {

        const val KEY_SELECTED_LOCATION = "selected_location"
    }
}