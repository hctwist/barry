package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.flow.first
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.Location

abstract class LocationUseCase<T>(
        private val selectedLocationRepository: SelectedLocationRepository,
        private val currentLocationRepository: CurrentLocationRepository,
        private val savedLocationsRepository: SavedLocationsRepository
) {

    abstract suspend fun invoke(location: Location): Outcome<T>

    suspend operator fun invoke(): Outcome<T> {

        return getLocation().switchMap {

            invoke(it)
        }
    }

    private suspend fun getLocation(): Outcome<Location> {

        val selectedId = selectedLocationRepository.getSelectedPlaceId().first()

        return if (selectedId == null) {

            currentLocationRepository.get()
        } else {

            savedLocationsRepository.getLocation(selectedId)?.asSuccess() ?: failure()
        }
    }
}