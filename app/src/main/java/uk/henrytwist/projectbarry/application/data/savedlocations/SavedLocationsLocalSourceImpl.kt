package uk.henrytwist.projectbarry.application.data.savedlocations

import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import uk.henrytwist.projectbarry.domain.models.SavedLocation
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsLocalSource
import javax.inject.Inject

class SavedLocationsLocalSourceImpl @Inject constructor(private val savedLocationDao: SavedLocationEntity.Dao) :
        SavedLocationsLocalSource {

    override suspend fun getLocation(placeId: String): SavedLocation? {

        return savedLocationDao.get(placeId)?.toSavedLocation()
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

    override suspend fun saveLocation(location: SavedLocation): Int {

        return savedLocationDao.insert(location.toEntity()).toInt()
    }

    override suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        savedLocationDao.updatePinned(location.placeId, pinned)
    }

    private fun SavedLocation.toEntity() = SavedLocationEntity(placeId, name, coordinates.lat, coordinates.lng, pinned)

    private fun SavedLocationEntity.toSavedLocation() = SavedLocation(placeId, name, LocationCoordinates(lat, lng), pinned)
}