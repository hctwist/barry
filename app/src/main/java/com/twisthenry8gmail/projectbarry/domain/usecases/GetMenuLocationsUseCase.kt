package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.successOrNull
import com.twisthenry8gmail.projectbarry.domain.data.locations.SavedLocationsRepository
import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import com.twisthenry8gmail.projectbarry.domain.uicore.LocationIcon
import com.twisthenry8gmail.projectbarry.domain.uicore.MenuLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetMenuLocationsUseCase @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
    private val selectedLocationRepository: SelectedLocationRepository
) {

    suspend fun getMenuLocations(): List<MenuLocation> {

        val selectedLocation = selectedLocationRepository.selectedLocation.value.successOrNull()

        val currentLocation = if (selectedLocationRepository.isSelectedLocationCurrent()) {

            val subtitle = selectedLocation?.let { it.locationData?.name } ?: ""
            MenuLocation.Current(true, LocationIcon.GPS_ON, subtitle)
        } else {

            MenuLocation.Current(false, LocationIcon.GPS_ON, "")
        }

        val pinnedLocations = savedLocationsRepository.getPinnedLocations()
        val selectedLocationId = selectedLocationRepository.getSelectedLocationId()

        val menuLocations = ArrayList<MenuLocation>(pinnedLocations.size + 1)
        menuLocations.add(currentLocation)
        menuLocations.addAll(pinnedLocations.map {

            MenuLocation.Saved(it.id == selectedLocationId, LocationIcon.PLACE, it.id, it.name, "")
        })

        return menuLocations
    }
}