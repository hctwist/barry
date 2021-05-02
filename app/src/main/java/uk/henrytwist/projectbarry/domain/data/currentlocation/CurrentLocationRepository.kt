package uk.henrytwist.projectbarry.domain.data.currentlocation

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource
import uk.henrytwist.projectbarry.domain.models.Location
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentLocationRepository @Inject constructor(
        private val currentLocationRemoteSource: CurrentLocationRemoteSource,
        private val geocodingRemoteSource: GeocodingRemoteSource
) {

    private val cacheMutex = Mutex()
    private var cache: TimestampedLocation? = null

    suspend fun get(): Outcome<Location> {

        cache.ifFresh { return it.asSuccess() }

        cacheMutex.withLock {

            cache.ifFresh { return it.asSuccess() }

            val location = currentLocationRemoteSource.getLocation().switchMap {

                geocodingRemoteSource.findLocation(it)
            }

            location.ifSuccessful { cache = TimestampedLocation(System.currentTimeMillis(), it) }

            return location
        }
    }

    private inline fun TimestampedLocation?.ifFresh(block: (Location) -> Unit) {

        if (this != null && this.time + LOCATION_CACHE_EXPIRY > System.currentTimeMillis()) {

            block(location)
        }
    }

    class TimestampedLocation(val time: Long, val location: Location)

    companion object {

        const val LOCATION_CACHE_EXPIRY = 600000
    }
}