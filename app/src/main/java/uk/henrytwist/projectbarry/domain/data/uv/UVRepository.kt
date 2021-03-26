package uk.henrytwist.projectbarry.domain.data.uv

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.asSuccess
import uk.henrytwist.kotlinbasics.failure
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.UV
import uk.henrytwist.projectbarry.domain.data.KeyedCacheRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class UVRepository @Inject constructor(
        private val remoteSource: UVRemoteSource,
        private val localSource: UVLocalSource
) : KeyedCacheRepository<Location, UV>() {

    override suspend fun isStale(value: UV): Boolean {

        // TODO Decide on value
        return value.time.until(Instant.now(), ChronoUnit.MINUTES) > 360
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