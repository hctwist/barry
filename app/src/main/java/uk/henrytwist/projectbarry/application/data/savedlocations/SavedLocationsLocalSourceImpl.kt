package uk.henrytwist.projectbarry.application.data.savedlocations

import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsLocalSource
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import uk.henrytwist.projectbarry.domain.models.SavedLocation
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

    override suspend fun saveLocation(location: Location, pin: Boolean): Int {

        return savedLocationDao.insert(SavedLocationEntity(
                0,
                location.name,
                location.coordinates.lat,
                location.coordinates.lng,
                pin
        )).toInt()
    }

    override suspend fun pinLocation(location: SavedLocation, pinned: Boolean) {

        savedLocationDao.updatePinned(location.id, pinned)
    }

    private fun SavedLocationEntity.toSavedLocation() = SavedLocation(id, name, LocationCoordinates(lat, lng), pinned)
}