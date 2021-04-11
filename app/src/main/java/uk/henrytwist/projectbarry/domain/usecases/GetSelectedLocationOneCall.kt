package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.flow.first
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.asSuccess
import uk.henrytwist.kotlinbasics.failure
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.SelectedLocation
import javax.inject.Inject

class GetSelectedLocationOneCall @Inject constructor(
        private val selectedLocationRepository: SelectedLocationRepository,
        private val currentLocationRepository: CurrentLocationRepository,
        private val savedLocationsRepository: SavedLocationsRepository
) {

    suspend operator fun invoke(): Outcome<SelectedLocation> {

        val selectedId = selectedLocationRepository.getSelectedPlaceId().first()

        return if (selectedId == null) {

            val location = currentLocationRepository.get()
            location.map { SelectedLocation(it, true) }
        } else {

            val location = savedLocationsRepository.getLocation(selectedId)
            if (location == null) {

                failure()
            } else {

                SelectedLocation(location, false).asSuccess()
            }
        }
    }
}