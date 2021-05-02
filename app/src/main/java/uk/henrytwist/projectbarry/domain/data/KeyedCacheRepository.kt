package uk.henrytwist.projectbarry.domain.data

import uk.henrytwist.kotlinbasics.outcomes.Outcome

abstract class KeyedCacheRepository<Key, Value> {

    suspend fun get(key: Key): Outcome<Value> {

        val local = fetchLocal(key)
        if (local is Outcome.Success) return local

        val remote = fetchRemote(key)
        remote.ifSuccessful {

            saveLocal(it)
        }
        return remote
    }

    protected abstract suspend fun saveLocal(value: Value)

    protected abstract suspend fun fetchLocal(key: Key): Outcome<Value>

    protected abstract suspend fun fetchRemote(key: Key): Outcome<Value>
}