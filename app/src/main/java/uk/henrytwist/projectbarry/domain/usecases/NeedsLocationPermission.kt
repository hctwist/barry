package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.flow.first
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import javax.inject.Inject

class NeedsLocationPermission @Inject constructor(private val selectedLocationLocalSource: SelectedLocationRepository) {

    suspend operator fun invoke(): Boolean {

        return selectedLocationLocalSource.getSelectedPlaceId().first() == null
    }
}