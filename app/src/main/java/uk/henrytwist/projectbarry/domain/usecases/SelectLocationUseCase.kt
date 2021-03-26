package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.MenuLocation
import uk.henrytwist.projectbarry.domain.models.SavedLocation
import javax.inject.Inject

class SelectLocationUseCase @Inject constructor(private val selectedLocationRepository: SelectedLocationRepository) {

    operator fun invoke(location: MenuLocation) {

        when (location) {

            is MenuLocation.Current -> selectedLocationRepository.selectCurrentLocation()
            is MenuLocation.Saved -> selectedLocationRepository.select(location.placeId)
        }
    }

    operator fun invoke(location: SavedLocation) {

        selectedLocationRepository.select(location.placeId)
    }
}