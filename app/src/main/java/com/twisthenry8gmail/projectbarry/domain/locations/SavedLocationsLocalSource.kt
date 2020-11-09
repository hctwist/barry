package com.twisthenry8gmail.projectbarry.domain.locations

import com.twisthenry8gmail.projectbarry.core.SavedLocation

interface SavedLocationsLocalSource {

    suspend fun getLocation(id: Int): SavedLocation?

    suspend fun getLocations(): List<SavedLocation>

    suspend fun getPinnedLocations(): List<SavedLocation>

    suspend fun saveLocation(location: SavedLocation): Int

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean)
}