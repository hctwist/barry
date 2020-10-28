package com.twisthenry8gmail.projectbarry.data

import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.success
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class OneShotForecastRepository<T> {

    private var cache = hashMapOf<RepositoryLocationData, T>()

    suspend fun get(location: LocationData): Result<T> {

        val repositoryLocation = RepositoryLocationData(location)
        val cacheResult = cache[repositoryLocation]

        if (cacheResult != null && isStale(cacheResult, location)) {

            val remote = fetchRemote(location)

            remote.ifSuccessful {

                saveLocal(it)
                cache[repositoryLocation] = it
            }

            return remote
        }

        return if (cacheResult == null) {

            val local = fetchLocal(location)
            if (local !is Result.Success || isStale(local.data, location)) {

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

    protected abstract suspend fun isStale(data: T, location: LocationData): Boolean

    protected abstract suspend fun saveLocal(data: T)

    protected abstract suspend fun fetchLocal(location: LocationData): Result<T>

    protected abstract suspend fun fetchRemote(location: LocationData): Result<T>

    data class RepositoryLocationData(val lat: Double, val lng: Double) {

        constructor(location: LocationData) : this(location.lat, location.lng)
    }
}