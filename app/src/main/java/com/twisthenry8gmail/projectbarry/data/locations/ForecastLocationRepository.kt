package com.twisthenry8gmail.projectbarry.data.locations

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.twisthenry8gmail.projectbarry.core.*
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private var autocompleteSessionToken: AutocompleteSessionToken? = null

    private val _selectedLocationFlow: MutableStateFlow<Result<ForecastLocation>> =
        MutableStateFlow(waiting())
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
                } else {

                    _selectedLocationFlow.value = success(selectedLocation)
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

    suspend fun unpin(placeId: String) {

        forecastLocationLocalSource.unpin(placeId)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): Result<ForecastLocation> {

        val lastLocation = getLastLocation()

        val locationResult =
            if (lastLocation is Result.Success) lastLocation else getFreshLocation()

        return locationResult.switchMap { location ->

            val lat = location.latitude
            val lng = location.longitude

            val locationDetails =
                forecastLocationRemoteSource.getLocationDetails(
                    lat,
                    lng,
                    autocompleteSessionToken
                )

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

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(): Result<Location> {

        return suspendCoroutine { cont ->

            locationClient.lastLocation.addOnCompleteListener {

                try {
                    val result = it.result

                    if (it.isSuccessful && result != null) {

                        cont.resume(success(result))
                    } else {

                        cont.resume(failure())
                    }
                } catch (e: Exception) {

                    cont.resume(failure())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(): Result<Location> {

        val request = LocationRequest.create().apply {

            numUpdates = 1
            interval = 4000L
        }

        return suspendCoroutine { cont ->

            locationClient.requestLocationUpdates(
                request,
                object : LocationCallback() {

                    override fun onLocationAvailability(availability: LocationAvailability?) {

                        if (availability?.isLocationAvailable != true) {

                            locationClient.removeLocationUpdates(this)
                            cont.resume(failure())
                        }
                    }

                    override fun onLocationResult(result: LocationResult?) {

                        locationClient.removeLocationUpdates(this)
                        if (result != null) {

                            val location = result.lastLocation
                            cont.resume(success(location))
                        } else {

                            cont.resume(failure())
                        }
                    }
                },
                Looper.getMainLooper()
            )
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

    suspend fun getPinnedAndChosenLocations(): List<ForecastLocation> {

        return forecastLocationLocalSource.getLocationsOf(
            listOf(
                ForecastLocation.Type.CHOSEN,
                ForecastLocation.Type.PINNED
            )
        )
    }

    suspend fun getPinnedLocations(): List<ForecastLocation> {

        return forecastLocationLocalSource.getLocationsOf(listOf(ForecastLocation.Type.PINNED))
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