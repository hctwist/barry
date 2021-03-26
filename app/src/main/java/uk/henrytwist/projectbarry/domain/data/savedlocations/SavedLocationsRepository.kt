package uk.henrytwist.projectbarry.domain.data.savedlocations

import uk.henrytwist.projectbarry.domain.models.SavedLocation

class SavedLocationsRepository(private val savedLocationsLocalSource: SavedLocationsLocalSource) {

    suspend fun getLocation(placeId: String): SavedLocation? {

        return savedLocationsLocalSource.getLocation(placeId)
    }

    suspend fun getLocations(): List<SavedLocation> {

        return savedLocationsLocalSource.getLocations()
    }

    suspend fun getPinnedLocations(): List<SavedLocation> {

        return savedLocationsLocalSource.getPinnedLocations()
    }

    suspend fun saveLocation(location: SavedLocation): Int {

        return savedLocationsLocalSource.saveLocation(location)
    }

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        return savedLocationsLocalSource.pinLocation(location, pinned)
    }
}