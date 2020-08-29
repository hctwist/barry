package com.twisthenry8gmail.projectbarry.data

import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.waiting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
abstract class ForecastRepository<T>(private val minRefreshInterval: Long) {

    private var lastRefreshed = 0L

    private val _flow = MutableStateFlow<Result<T>>(waiting())
    val flow: StateFlow<Result<T>>
        get() = _flow

    suspend fun fetch(location: ForecastLocation) {

        val local = fetchLocal(location)
        if (local is Result.Failure) {

            refresh(location)
        } else {

            _flow.value = local
        }
    }

    suspend fun refresh(location: ForecastLocation) {

        if (_flow.value !is Result.Success || System.currentTimeMillis() - lastRefreshed > minRefreshInterval) {

            val remote = fetchRemote(location)
            remote.ifSuccessful {

                saveLocal(it)
            }
            lastRefreshed = System.currentTimeMillis()
            _flow.value = remote
        }
    }

    protected abstract suspend fun saveLocal(data: T)

    protected abstract suspend fun fetchLocal(location: ForecastLocation): Result<T>

    protected abstract suspend fun fetchRemote(location: ForecastLocation): Result<T>
}