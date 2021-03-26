package uk.henrytwist.projectbarry.domain.data

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.success

abstract class KeyedCacheRepository<Key, Value> {

    private var cache = hashMapOf<Key, Value>()

    suspend fun get(key: Key): Outcome<Value> {

        val cacheResult = cache[key]

        return if (cacheResult != null && isStale(cacheResult)) {

            fetchRemoteAndCache(key)
        } else if (cacheResult == null) {

            val local = fetchLocal(key)
            if (local !is Outcome.Success || isStale(local.data)) {

                fetchRemoteAndCache(key)
            } else {

                cache[key] = local.data
                local
            }
        } else {

            success(cacheResult)
        }
    }

    private suspend fun fetchRemoteAndCache(key: Key): Outcome<Value> {

        val remote = fetchRemote(key)
        remote.ifSuccessful {

            saveLocal(it)
            cache[key] = it
        }
        return remote
    }

    protected abstract suspend fun isStale(value: Value): Boolean

    protected abstract suspend fun saveLocal(value: Value)

    protected abstract suspend fun fetchLocal(key: Key): Outcome<Value>

    protected abstract suspend fun fetchRemote(key: Key): Outcome<Value>
}