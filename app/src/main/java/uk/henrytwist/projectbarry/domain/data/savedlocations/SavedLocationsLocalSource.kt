package uk.henrytwist.projectbarry.domain.data.savedlocations

import uk.henrytwist.projectbarry.domain.models.SavedLocation

interface SavedLocationsLocalSource {

    suspend fun getLocation(placeId: String): SavedLocation?

    suspend fun getLocations(): List<SavedLocation>

    suspend fun getPinnedLocations(): List<SavedLocation>

    suspend fun saveLocation(location: SavedLocation): Int

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean)
}