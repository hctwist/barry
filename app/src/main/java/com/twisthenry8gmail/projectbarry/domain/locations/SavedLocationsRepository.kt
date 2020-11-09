package com.twisthenry8gmail.projectbarry.domain.locations

import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.locations.SavedLocationsLocalSource
import javax.inject.Inject

class SavedLocationsRepository @Inject constructor(private val savedLocationsLocalSource: SavedLocationsLocalSource) {

    suspend fun getLocation(id: Int): SavedLocation? {

        return savedLocationsLocalSource.getLocation(id)
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