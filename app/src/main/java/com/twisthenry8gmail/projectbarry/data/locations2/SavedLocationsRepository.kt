package com.twisthenry8gmail.projectbarry.data.locations2

import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.core.SavedLocation
import javax.inject.Inject

class SavedLocationsRepository @Inject constructor(private val savedLocationDao: SavedLocationEntity.Dao) {

    suspend fun getLocation(id: Int): SavedLocation? {

        return savedLocationDao.get(id)?.toSavedLocation()
    }

    suspend fun getLocations(): List<SavedLocation> {

        return savedLocationDao.getAll().map {

            it.toSavedLocation()
        }
    }

    suspend fun getPinnedLocations(): List<SavedLocation> {

        return savedLocationDao.getPinned().map {

            it.toSavedLocation()
        }
    }

    suspend fun saveLocation(location: LocationData): Int {

        return savedLocationDao.insert(location.toEntity()).toInt()
    }

    suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        savedLocationDao.updatePinned(location.id, pinned)
    }

    private fun LocationData.toEntity() = SavedLocationEntity(name, lat, lng, false)

    private fun SavedLocationEntity.toSavedLocation() = SavedLocation(id, name, lat, lng, pinned)
}