package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.data.locations2.ForecastLocationRepository2
import com.twisthenry8gmail.projectbarry.data.locations2.SavedLocationsRepository
import com.twisthenry8gmail.projectbarry.data.locations2.SelectedLocationLocalSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SaveLocationUseCase @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
    private val selectedLocationLocalSource: SelectedLocationLocalSource,
    private val locationRepository: ForecastLocationRepository2
) {

    suspend operator fun invoke(searchResult: LocationSearchResult) {

        val location = locationRepository.getLocation(searchResult.placeId)

        location.ifSuccessful {

            val id = savedLocationsRepository.saveLocation(it)
            selectedLocationLocalSource.select(id)
            locationRepository.select(SelectedLocation(SelectedLocation.Status.STATIC_LOCATION, it))
        }
    }
}