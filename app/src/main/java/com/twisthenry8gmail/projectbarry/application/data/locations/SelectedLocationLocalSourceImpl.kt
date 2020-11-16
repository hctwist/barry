package com.twisthenry8gmail.projectbarry.application.data.locations

import android.content.SharedPreferences
import com.twisthenry8gmail.projectbarry.application.data.SharedPreferencesModule
import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationLocalSource
import javax.inject.Inject

class SelectedLocationLocalSourceImpl @Inject constructor(@SharedPreferencesModule.Data private val dataPreferences: SharedPreferences) :
    SelectedLocationLocalSource {

    override fun getSelectedLocationId(): Int? {

        return if (!dataPreferences.contains(CHOSEN_LOCATION)) {

            null
        } else {

            dataPreferences.getInt(CHOSEN_LOCATION, 0)
        }
    }

    override fun select(id: Int) {

        dataPreferences.edit().putInt(CHOSEN_LOCATION, id).apply()
    }

    override fun removeSelection() {

        dataPreferences.edit().remove(CHOSEN_LOCATION).apply()
    }

    companion object {

        private const val CHOSEN_LOCATION = "chosen_location"
    }
}