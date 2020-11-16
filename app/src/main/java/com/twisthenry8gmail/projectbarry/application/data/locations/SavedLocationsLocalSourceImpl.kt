package com.twisthenry8gmail.projectbarry.application.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.LocationData
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.data.locations.SavedLocationsLocalSource
import javax.inject.Inject

class SavedLocationsLocalSourceImpl @Inject constructor(private val savedLocationDao: SavedLocationEntity.Dao) :
    SavedLocationsLocalSource {

    override suspend fun getLocation(id: Int): SavedLocation? {

        return savedLocationDao.get(id)?.toSavedLocation()
    }

    override suspend fun getLocations(): List<SavedLocation> {

        return savedLocationDao.getAll().map {

            it.toSavedLocation()
        }
    }

    override suspend fun getPinnedLocations(): List<SavedLocation> {

        return savedLocationDao.getPinned().map {

            it.toSavedLocation()
        }
    }

    override suspend fun saveLocation(location: LocationData): Int {

        return savedLocationDao.insert(location.toEntity()).toInt()
    }

    override suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        savedLocationDao.updatePinned(location.id, pinned)
    }

    private fun LocationData.toEntity() = SavedLocationEntity(name, lat, lng, false)

    private fun SavedLocationEntity.toSavedLocation() = SavedLocation(id, name, lat, lng, pinned)
}