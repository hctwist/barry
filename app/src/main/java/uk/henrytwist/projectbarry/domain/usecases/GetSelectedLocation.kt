package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.asSuccess
import uk.henrytwist.kotlinbasics.failure
import uk.henrytwist.kotlinbasics.waiting
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.SelectedLocation
import javax.inject.Inject

@Deprecated("Replaced with GetSelectedLocationOneCall")
@ExperimentalCoroutinesApi
class GetSelectedLocation @Inject constructor(
        private val selectedLocationRepository: SelectedLocationRepository,
        private val currentLocationRepository: CurrentLocationRepository,
        private val savedLocationsRepository: SavedLocationsRepository
) {

    operator fun invoke(): Flow<Outcome<SelectedLocation>> {

        val selectedIdFlow = selectedLocationRepository.getSelectedPlaceId()

        return selectedIdFlow.flatMapLatest { selectedId ->
            if (selectedId == null) {

                flow {

                    emit(SelectedLocation(null, true).asSuccess())

                    val location = currentLocationRepository.get()
                    emit(location.map { SelectedLocation(it, true) })
                }
            } else {

                flow {

                    emit(waiting())

                    val location = savedLocationsRepository.getLocation(selectedId)
                    if (location == null) {

                        emit(failure())
                    } else {

                        emit(SelectedLocation(location, false).asSuccess())
                    }
                }
            }
        }
    }
}