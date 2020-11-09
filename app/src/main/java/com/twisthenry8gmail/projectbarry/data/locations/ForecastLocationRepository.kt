package com.twisthenry8gmail.projectbarry.data.locations

import com.twisthenry8gmail.projectbarry.core.*
import com.twisthenry8gmail.projectbarry.domain.locations.SavedLocationsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

// TODO Dependant on another repository?
@ExperimentalCoroutinesApi
@Singleton
class ForecastLocationRepository @Inject constructor(
    private val selectedLocationLocalSource: SelectedLocationLocalSourceImpl,
    private val currentLocationLocalSource: CurrentLocationLocalSource,
    private val currentLocationRemoteSource: CurrentLocationRemoteSourceImpl,
    private val savedLocationsLocalSource: SavedLocationsRepository,
    private val remoteSource: ForecastLocationRemoteSource
) {

    private val _selectedLocation: MutableStateFlow<Result<SelectedLocation>> =
        MutableStateFlow(waiting())
    val selectedLocation: StateFlow<Result<SelectedLocation>>
        get() = _selectedLocation

    suspend fun fetchSelectedLocation() {

        val selectedId = getSelectedLocationId()

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

                remoteSource.findLocationDetails(
                    lastLocation.latitude,
                    lastLocation.longitude
                ).map {

                    SelectedLocation(
                        SelectedLocation.Status.INTERMEDIATE_CURRENT_LOCATION,
                        LocationData(it.name, lastLocation.latitude, lastLocation.longitude)
                    )
                }
            }
        if (selectedLocationLast is Result.Success) {

            _selectedLocation.value = selectedLocationLast
        }

        // Location update
        val selectedLocationUpdate =
            currentLocationRemoteSource.getLocationUpdate().switchMap { locationUpdate ->

                remoteSource.findLocationDetails(
                    locationUpdate.latitude,
                    locationUpdate.longitude
                ).map {

                    SelectedLocation(
                        SelectedLocation.Status.CURRENT_LOCATION,
                        LocationData(it.name, locationUpdate.latitude, locationUpdate.longitude)
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

    suspend fun search(query: String): Result<List<LocationSearchResult>> {

        return remoteSource.search(query).mapEach {

            LocationSearchResult(
                it.placeId,
                it.getPrimaryText(null).toString(),
                it.getSecondaryText(null).toString()
            )
        }
    }

    suspend fun getSelectedLocationId(): Int? {

        return selectedLocationLocalSource.getSelectedLocationId()
    }

    suspend fun getLocation(placeId: String): Result<LocationData> {

        return remoteSource.findLocationDetails(placeId).map {

            LocationData(it.name, it.lat, it.lng)
        }
    }

    fun select(selectedLocation: SelectedLocation) {

        _selectedLocation.value = success(selectedLocation)
    }

    fun select(location: SavedLocation) {

        selectedLocationLocalSource.select(location.id)
        _selectedLocation.value =
            success(SelectedLocation(SelectedLocation.Status.STATIC_LOCATION, location))
    }

    suspend fun selectCurrentLocation() {

        selectedLocationLocalSource.removeSelection()
        fetchCurrentLocation()
    }
}