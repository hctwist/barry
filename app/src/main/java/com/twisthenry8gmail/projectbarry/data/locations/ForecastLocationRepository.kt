package com.twisthenry8gmail.projectbarry.data.locations

import android.content.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import com.twisthenry8gmail.projectbarry.failure
import com.twisthenry8gmail.projectbarry.loading
import com.twisthenry8gmail.projectbarry.success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
@Singleton
class ForecastLocationRepository @Inject constructor(
    private val forecastLocationLocalSource: ForecastLocationLocalSource,
    private val forecastLocationRemoteSource: ForecastLocationRemoteSource,
    private val locationClient: FusedLocationProviderClient,
    @SharedPreferencesModule.Data private val dataPrefs: SharedPreferences
) {

    private var savedLocationsJob: Deferred<List<ForecastLocation>>? = null

    private var autocompleteSessionToken: AutocompleteSessionToken? = null

    private val _selectedLocationFlow: MutableStateFlow<Result<ForecastLocation>> =
        MutableStateFlow(loading())
    val selectedLocationFlow: StateFlow<Result<ForecastLocation>>
        get() = _selectedLocationFlow

    fun getSelectedPlaceId(): String? {

        return dataPrefs.getString(KEY_SELECTED_LOCATION, null)
    }

    suspend fun getSelectedLocationType(): Result<ForecastLocation.Type> {

        val selectedId = getSelectedPlaceId()
        val type = selectedId?.let { forecastLocationLocalSource.getLocationType(selectedId) }

        return if (type == null) failure() else success(type)
    }

    suspend fun fetchSelectedLocation() {

        val selectedId = getSelectedPlaceId()

        if (selectedId == null) {

            val currentLocation = getCurrentLocation()

            currentLocation.ifSuccessful {

                replaceAllOfType(it.copy(type = ForecastLocation.Type.LAST_KNOWN_LOCATION))
                select(it.placeId)
            }

            _selectedLocationFlow.value = currentLocation
        } else {

            val selectedLocation = forecastLocationLocalSource.getLocation(selectedId)

            if (selectedLocation == null) {

                _selectedLocationFlow.value = failure()
            } else {

                if (selectedLocation.type == ForecastLocation.Type.LAST_KNOWN_LOCATION) {

                    _selectedLocationFlow.value =
                        success(
                            selectedLocation.copy(type = ForecastLocation.Type.PENDING_LOCATION)
                        )

                    val currentLocation = getCurrentLocation()

                    _selectedLocationFlow.value = if (currentLocation is Result.Success) {

                        replaceAllOfType(currentLocation.data.copy(type = ForecastLocation.Type.LAST_KNOWN_LOCATION))
                        select(currentLocation.data.placeId)
                        currentLocation
                    } else {

                        success(selectedLocation)
                    }
                }
            }
        }
    }

    suspend fun select(placeId: String) {

        val location = getLocation(placeId)
        if (location is Result.Success) {

            _selectedLocationFlow.value = location
            dataPrefs.edit().putString(KEY_SELECTED_LOCATION, placeId).apply()
        }
    }

    suspend fun removeSelection() {

        dataPrefs.edit().remove(KEY_SELECTED_LOCATION).apply()
        fetchSelectedLocation()
    }

    suspend fun pin(placeId: String) {

        forecastLocationLocalSource.pin(placeId)
    }

    private suspend fun getCurrentLocation(): Result<ForecastLocation> {

        val latLngResult = suspendCoroutine<Result<Pair<Double, Double>>> { cont ->

            try {
                locationClient.lastLocation.addOnCompleteListener {

                    val result = it.result
                    if (it.isSuccessful && result != null) {

                        cont.resume(
                            success(
                                result.latitude to result.longitude
                            )
                        )
                    } else {

                        cont.resume(failure())
                    }
                }
            } catch (securityException: SecurityException) {

                cont.resume(failure())
            }
        }

        return latLngResult.switchMap { latLng ->

            val lat = latLng.first
            val lng = latLng.second

            val locationDetails =
                forecastLocationRemoteSource.getLocationDetails(lat, lng, autocompleteSessionToken)

            if (locationDetails is Result.Success) {

                forecastLocationLocalSource.replaceAllOfType(locationDetails.data.let {

                    ForecastLocation(
                        it.placeId,
                        it.name,
                        it.lat,
                        it.lng,
                        ForecastLocation.Type.LAST_KNOWN_LOCATION
                    )
                })
            }

            locationDetails.map {

                ForecastLocation(
                    it.placeId,
                    it.name,
                    it.lat,
                    it.lng,
                    ForecastLocation.Type.CURRENT_LOCATION
                )
            }
        }
    }

    suspend fun getLocation(placeId: String): Result<ForecastLocation> {

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