package com.twisthenry8gmail.projectbarry.data

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.success

@Deprecated("Unused")
class CachedData<T> {

    private var dirty = false
    private var cache: T? = null

    fun invalidate() {

        dirty = true
    }

    // TODO Multiple calls whilst fetching
    suspend fun get(
        fetchLocal: suspend () -> Result<T>,
        saveLocal: suspend (T) -> Unit,
        fetchRemote: suspend () -> Result<T>,
        cachedDataValid: suspend (T) -> Boolean
    ): Result<T> {

        return if (cache != null && !dirty && cachedDataValid(cache!!)) {

            success(cache!!)
        } else if (dirty) {

            val remote = fetchRemote()

            if (remote is Result.Success) {

                cache = remote.data
                saveLocal(remote.data)
                dirty = false
            }

            return remote
        } else {

            val local = fetchLocal()

            return if (local is Result.Success && cachedDataValid(local.data)) {

                cache = local.data
                local
            } else {

                val remote = fetchRemote()

                if (remote is Result.Success) {

                    cache = remote.data
                    saveLocal(remote.data)
                }

                return remote
            }
        }
    }
}