package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import uk.henrytwist.projectbarry.domain.models.SavedLocation
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import javax.inject.Inject

class SaveSearchLocation @Inject constructor(
        private val savedLocationsRepository: SavedLocationsRepository,
        private val selectedLocationRepository: SelectedLocationRepository,
        private val searchRepository: LocationSearchRepository<*>
) {

    suspend operator fun invoke(searchResult: LocationSearchResult) {

        val location = searchRepository.getLocation(searchResult)

        location.ifSuccessful {

            savedLocationsRepository.saveLocation(SavedLocation(it.placeId, it.name, it.coordinates, false))
            selectedLocationRepository.select(it.placeId)
        }
    }
}