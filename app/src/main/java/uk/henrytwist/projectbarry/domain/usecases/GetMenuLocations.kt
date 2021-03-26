package uk.henrytwist.projectbarry.domain.usecases

import kotlinx.coroutines.flow.first
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationRepository
import uk.henrytwist.projectbarry.domain.models.MenuLocation
import javax.inject.Inject

class GetMenuLocations @Inject constructor(
        private val savedLocationsRepository: SavedLocationsRepository,
        private val selectedLocationRepository: SelectedLocationRepository
) {

    suspend fun getMenuLocations(): List<MenuLocation> {

        val selectedLocationId = selectedLocationRepository.getSelectedPlaceId().first()

        val currentLocation = if (selectedLocationId == null) {

            MenuLocation.Current(true)
        } else {

            MenuLocation.Current(false)
        }

        val pinnedLocations = savedLocationsRepository.getPinnedLocations()

        val menuLocations = ArrayList<MenuLocation>(pinnedLocations.size + 1)
        menuLocations.add(currentLocation)
        menuLocations.addAll(pinnedLocations.map {

            MenuLocation.Saved(it.placeId == selectedLocationId, it.placeId, it.name)
        })

        return menuLocations
    }
}