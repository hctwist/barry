package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.data.locations.LocationAutocompleteRepository
import com.twisthenry8gmail.projectbarry.domain.data.locations.SavedLocationsRepository
import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SaveSearchLocationUseCase @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
    private val selectedLocationRepository: SelectedLocationRepository,
    private val autocompleteRepository: LocationAutocompleteRepository<*>
) {

    suspend operator fun invoke(searchResult: LocationSearchResult) {

        val location = autocompleteRepository.getLocation(searchResult)

        location.ifSuccessful {

            val id = savedLocationsRepository.saveLocation(it)

            val savedLocation = SavedLocation(
                id,
                it.name,
                it.lat,
                it.lng,
                false
            )

            selectedLocationRepository.select(savedLocation.id)
        }
    }
}