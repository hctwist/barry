package com.twisthenry8gmail.projectbarry.data.locations2

import android.content.SharedPreferences
import com.twisthenry8gmail.projectbarry.data.SharedPreferencesModule
import javax.inject.Inject

// TODO Use this in ForecastLocationRepository2 only
class SelectedLocationLocalSource @Inject constructor(@SharedPreferencesModule.Data private val dataPreferences: SharedPreferences) {

    fun getSelectedLocationId(): Int? {

        return if (!dataPreferences.contains(CHOSEN_LOCATION)) {

            null
        } else {

            dataPreferences.getInt(CHOSEN_LOCATION, 0)
        }
    }

    fun select(id: Int) {

        dataPreferences.edit().putInt(CHOSEN_LOCATION, id).apply()
    }

    fun removeSelection() {

        dataPreferences.edit().remove(CHOSEN_LOCATION).apply()
    }

    companion object {

        private const val CHOSEN_LOCATION = "chosen_location"
    }
}