package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.flow.first
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.Location
import javax.inject.Inject

class LocationUseCaseHelper @Inject constructor(
        private val selectedLocationRepository: SelectedLocationRepository,
        private val currentLocationRepository: CurrentLocationRepository,
        private val savedLocationsRepository: SavedLocationsRepository
) {

    suspend fun getLocation(): Outcome<Location> {

        val selectedId = selectedLocationRepository.getSelectedLocationId().first()

        return if (selectedId == null) {

            currentLocationRepository.get()
        } else {

            savedLocationsRepository.getLocation(selectedId)?.asSuccess() ?: failure()
        }
    }
}