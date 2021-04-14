package uk.henrytwist.projectbarry.domain.data.uv

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.projectbarry.domain.data.KeyedCacheRepository
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.UV
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@Deprecated("Replaced by OpenWeather API")
class UVRepository @Inject constructor(
        private val remoteSource: UVRemoteSource,
        private val localSource: UVLocalSource
) : KeyedCacheRepository<Location, UV>() {

    override suspend fun isStale(value: UV): Boolean {

        // TODO Decide on value
        return value.time.until(Instant.now(), ChronoUnit.MINUTES) > 60
    }

    override suspend fun saveLocal(value: UV) {

        localSource.save(value)
    }

    override suspend fun fetchLocal(key: Location): Outcome<UV> {

        return localSource.get(key.placeId)?.asSuccess() ?: failure()
    }

    override suspend fun fetchRemote(key: Location): Outcome<UV> {

        return remoteSource.get(key)
    }
}