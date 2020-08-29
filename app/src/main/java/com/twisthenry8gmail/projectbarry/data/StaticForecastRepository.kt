package com.twisthenry8gmail.projectbarry.data

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.success
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class StaticForecastRepository<T> {

    private var cache = hashMapOf<RepositoryLocationData, T>()

    suspend fun get(location: ForecastLocation): Result<T> {

        val repositoryLocation = RepositoryLocationData(location)
        val cacheResult = cache[repositoryLocation]

        if (cacheResult != null && isStale(cacheResult)) {

            val remote = fetchRemote(location)

            remote.ifSuccessful {

                saveLocal(it)
                cache[repositoryLocation] = it
            }

            return remote
        }

        return if (cacheResult == null) {

            val local = fetchLocal(location)
            if (local !is Result.Success || isStale(local.data)) {

                val remote = fetchRemote(location)
                remote.ifSuccessful {

                    saveLocal(it)
                    cache[repositoryLocation] = it
                }
                remote
            } else {

                cache[repositoryLocation] = local.data
                local
            }
        } else {

            success(cacheResult)
        }
    }

    protected abstract suspend fun isStale(data: T): Boolean

    protected abstract suspend fun saveLocal(data: T)

    protected abstract suspend fun fetchLocal(location: ForecastLocation): Result<T>

    protected abstract suspend fun fetchRemote(location: ForecastLocation): Result<T>

    data class RepositoryLocationData(val lat: Double, val lng: Double) {

        constructor(location: ForecastLocation) : this(location.lat, location.lng)
    }
}