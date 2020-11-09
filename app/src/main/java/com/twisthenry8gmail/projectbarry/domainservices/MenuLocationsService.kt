package com.twisthenry8gmail.projectbarry.domainservices

import com.twisthenry8gmail.projectbarry.core.successOrNull
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.domain.locations.SavedLocationsRepository
import com.twisthenry8gmail.projectbarry.uicore.LocationIcon
import com.twisthenry8gmail.projectbarry.uicore.MenuLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MenuLocationsService @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
    private val forecastLocationRepository: ForecastLocationRepository
) {

    suspend fun getMenuLocations(): List<MenuLocation> {

        val selectedLocationId = forecastLocationRepository.getSelectedLocationId()
        val currentLocation = if (selectedLocationId == null) {

            val selectedLocation = forecastLocationRepository.selectedLocation.value
            val subtitle = selectedLocation.successOrNull()?.let { it.locationData?.name } ?: ""
            MenuLocation.Current(true, LocationIcon.GPS_ON, subtitle)
        } else {

            MenuLocation.Current(false, LocationIcon.GPS_ON, "")
        }

        val pinnedLocations = savedLocationsRepository.getPinnedLocations()

        val menuLocations = ArrayList<MenuLocation>(pinnedLocations.size + 1)
        menuLocations.add(currentLocation)
        menuLocations.addAll(pinnedLocations.map {

            MenuLocation.Saved(it.id == selectedLocationId, LocationIcon.PLACE, it.id, it.name, "")
        })

        return menuLocations
    }
}