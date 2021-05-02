package uk.henrytwist.projectbarry.domain.data.savedlocations

import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.SavedLocation

class SavedLocationsRepository(private val savedLocationsLocalSource: SavedLocationsLocalSource) {

    suspend fun getLocation(id: Int): SavedLocation? {

        return savedLocationsLocalSource.getLocation(id)
    }

    suspend fun getLocations(): List<SavedLocation> {

        return savedLocationsLocalSource.getLocations()
    }

    suspend fun getPinnedLocations(): List<SavedLocation> {

        return savedLocationsLocalSource.getPinnedLocations()
    }

    suspend fun saveLocation(location: Location, pin: Boolean): Int {

        return savedLocationsLocalSource.saveLocation(location, pin)
    }

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        return savedLocationsLocalSource.pinLocation(location, pinned)
    }
}