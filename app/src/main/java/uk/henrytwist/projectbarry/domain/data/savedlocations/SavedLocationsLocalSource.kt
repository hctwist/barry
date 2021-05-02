package uk.henrytwist.projectbarry.domain.data.savedlocations

import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.SavedLocation

interface SavedLocationsLocalSource {

    suspend fun getLocation(id: Int): SavedLocation?

    suspend fun getLocations(): List<SavedLocation>

    suspend fun getPinnedLocations(): List<SavedLocation>

    suspend fun saveLocation(location: Location, pin: Boolean): Int

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean)
}