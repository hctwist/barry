package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import javax.inject.Inject

class GetSelectedLocationInvalidationTracker @Inject constructor(private val selectedLocationRepository: SelectedLocationRepository) {

    operator fun invoke() = selectedLocationRepository.getSelectedLocationId()
}