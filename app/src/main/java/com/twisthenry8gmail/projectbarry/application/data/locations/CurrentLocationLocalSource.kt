package com.twisthenry8gmail.projectbarry.application.data.locations

import android.content.SharedPreferences
import com.google.gson.Gson
import com.twisthenry8gmail.projectbarry.domain.core.LocationData

// TODO Use this AND HASN'T BEEN MIGRATED TO INTERFACES
class CurrentLocationLocalSource constructor(private val dataPreferences: SharedPreferences) {

    private val gson = Gson()

    fun save(location: LocationData) {

        val json = gson.toJson(location)
        dataPreferences.edit().putString(CACHED_LOCATION, json).apply()
    }

    fun get(): LocationData? {

        val json = dataPreferences.getString(CACHED_LOCATION, null)
        return json?.let { gson.fromJson(json, LocationData::class.java) }
    }

    companion object {

        private const val CACHED_LOCATION = "cached_current_location"
    }
}