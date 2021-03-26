package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.models.SavedLocation
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import javax.inject.Inject

class TogglePinLocationUseCase @Inject constructor(private val savedLocationsRepository: SavedLocationsRepository) {

    suspend operator fun invoke(location: SavedLocation) {

        savedLocationsRepository.pinLocation(location, !location.pinned)
    }
}