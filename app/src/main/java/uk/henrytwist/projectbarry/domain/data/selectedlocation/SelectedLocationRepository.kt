package uk.henrytwist.projectbarry.domain.data.selectedlocation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedLocationRepository @Inject constructor(private val localSource: SelectedLocationLocalSource) {

    private val selectedPlaceId = MutableStateFlow(localSource.getSelectedLocationId())

    fun getSelectedPlaceId(): Flow<String?> = selectedPlaceId

    fun select(placeId: String) {

        selectedPlaceId.value = placeId
        localSource.select(placeId)
    }

    fun selectCurrentLocation() {

        selectedPlaceId.value = null
        localSource.removeSelection()
    }
}