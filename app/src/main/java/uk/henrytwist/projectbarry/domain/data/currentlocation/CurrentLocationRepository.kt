package uk.henrytwist.projectbarry.domain.data.currentlocation

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.asSuccess
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource
import uk.henrytwist.projectbarry.domain.models.Location
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentLocationRepository @Inject constructor(
        private val currentLocationRemoteSource: CurrentLocationRemoteSource,
        private val geocodingRemoteSource: GeocodingRemoteSource
) {

    // TODO This cache should expire
    private val cacheMutex = Mutex()
    private var cache: Location? = null

    suspend fun get(): Outcome<Location> {

        cache?.let { return it.asSuccess() }

        cacheMutex.withLock {

            cache?.let { return it.asSuccess() }

//            val lastLocation = currentLocationRemoteSource.getLastLocation()
//            val upToDateLocation = if (lastLocation is Outcome.Success) lastLocation else currentLocationRemoteSource.getLocationUpdate()

            val upToDateLocation = currentLocationRemoteSource.getLocation()

            val location = upToDateLocation.switchMap {

                geocodingRemoteSource.findLocation(it)
            }

            location.ifSuccessful { cache = it }

            return location
        }
    }
}