package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.LocationData
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation

interface SavedLocationsLocalSource {

    suspend fun getLocation(id: Int): SavedLocation?

    suspend fun getLocations(): List<SavedLocation>

    suspend fun getPinnedLocations(): List<SavedLocation>

    suspend fun saveLocation(location: LocationData): Int

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean)
}