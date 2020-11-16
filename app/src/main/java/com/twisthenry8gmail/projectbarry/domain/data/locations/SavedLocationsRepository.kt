package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.LocationData
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation

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

    suspend fun saveLocation(location: LocationData): Int {

        return savedLocationsLocalSource.saveLocation(location)
    }

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        return savedLocationsLocalSource.pinLocation(location, pinned)
    }
}