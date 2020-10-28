package com.twisthenry8gmail.projectbarry.data.locations2

import android.content.SharedPreferences
import com.google.gson.Gson
import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import javax.inject.Inject

// TODO Use this
class CurrentLocationLocalSource @Inject constructor(@SharedPreferencesModule.Data val dataPreferences: SharedPreferences) {

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