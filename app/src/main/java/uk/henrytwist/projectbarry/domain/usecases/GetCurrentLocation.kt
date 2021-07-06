package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRepository
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.SelectedLocation
import javax.inject.Inject

class GetCurrentLocation @Inject constructor(
        private val currentLocationRepository: CurrentLocationRepository
) {

    suspend operator fun invoke(): Outcome<Location> {

        return currentLocationRepository.get()
    }
}