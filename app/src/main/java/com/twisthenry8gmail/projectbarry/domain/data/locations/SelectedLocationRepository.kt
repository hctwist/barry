package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class SelectedLocationRepository(
    private val selectedLocationLocalSource: SelectedLocationLocalSource,
    private val savedLocationsLocalSource: SavedLocationsLocalSource,
    private val currentLocationRemoteSource: CurrentLocationRemoteSource,
    private val geocodingRemoteSource: GeocodingRemoteSource
) {

    private val _selectedLocation: MutableStateFlow<Result<SelectedLocation>> =
        MutableStateFlow(waiting())
    val selectedLocation: StateFlow<Result<SelectedLocation>>
        get() = _selectedLocation

    suspend fun refreshSelectedLocation() {

        val selectedId = selectedLocationLocalSource.getSelectedLocationId()

        if (selectedId == null) {

            fetchCurrentLocation()
        } else {

            val location = savedLocationsLocalSource.getLocation(selectedId)
            if (location == null) {

                _selectedLocation.value = failure()
            } else {

                _selectedLocation.value =
                    SelectedLocation(SelectedLocation.Status.STATIC_LOCATION, location).asSuccess()
            }
        }
    }

    private suspend fun fetchCurrentLocation() {

        _selectedLocation.value =
            success(SelectedLocation(SelectedLocation.Status.INTERMEDIATE_CURRENT_LOCATION, null))

        // Last location
        val selectedLocationLast =
            currentLocationRemoteSource.getLastLocation().switchMap { lastLocation ->

                geocodingRemoteSource.findLocationDetails(
                    lastLocation.lat,
                    lastLocation.lng
                ).map {

                    SelectedLocation(
                        SelectedLocation.Status.INTERMEDIATE_CURRENT_LOCATION,
                        LocationData(it.name, lastLocation.lat, lastLocation.lng)
                    )
                }
            }
        if (selectedLocationLast is Result.Success) {

            _selectedLocation.value = selectedLocationLast
        }

        // Location update
        val selectedLocationUpdate =
            currentLocationRemoteSource.getLocationUpdate().switchMap { locationUpdate ->

                geocodingRemoteSource.findLocationDetails(
                    locationUpdate.lat,
                    locationUpdate.lng
                ).map {

                    SelectedLocation(
                        SelectedLocation.Status.CURRENT_LOCATION,
                        LocationData(it.name, locationUpdate.lat, locationUpdate.lng)
                    )
                }
            }

        _selectedLocation.value = when {
            selectedLocationUpdate is Result.Success -> {

                selectedLocationUpdate
            }
            selectedLocationLast is Result.Success -> {

                selectedLocationLast.map {
                    SelectedLocation(
                        SelectedLocation.Status.OUTDATED_CURRENT_LOCATION,
                        it.locationData
                    )
                }
            }
            else -> failure()
        }
    }

    // TODO Use cache?
    fun isSelectedLocationCurrent(): Boolean {

        return selectedLocationLocalSource.getSelectedLocationId() == null
    }

    fun getSelectedLocationId(): Int? {

        return selectedLocationLocalSource.getSelectedLocationId()
    }

    suspend fun select(id: Int) {

        selectedLocationLocalSource.select(id)
        val location = savedLocationsLocalSource.getLocation(id)
        _selectedLocation.value =
            success(SelectedLocation(SelectedLocation.Status.STATIC_LOCATION, location))
    }

    suspend fun selectCurrentLocation() {

        selectedLocationLocalSource.removeSelection()
        fetchCurrentLocation()
    }
}