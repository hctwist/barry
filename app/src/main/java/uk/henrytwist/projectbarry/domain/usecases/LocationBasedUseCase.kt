package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.waiting
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.SelectedLocation

abstract class LocationBasedUseCase<T> {

    operator fun invoke(selectedLocation: Flow<Outcome<SelectedLocation>>): Flow<Outcome<T>> {

        return selectedLocation.map { selectedLocationOutcome ->

            selectedLocationOutcome.switchMap { selectedLocation ->

                val location = selectedLocation.location
                if (location == null) {

                    waiting()
                } else {

                    invoke(location)
                }
            }
        }
    }

    protected abstract suspend fun invoke(location: Location): Outcome<T>
}