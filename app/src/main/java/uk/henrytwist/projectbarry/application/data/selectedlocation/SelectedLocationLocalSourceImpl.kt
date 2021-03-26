package uk.henrytwist.projectbarry.application.data.selectedlocation

import android.content.SharedPreferences
import uk.henrytwist.projectbarry.application.di.SharedPreferencesModule
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationLocalSource
import javax.inject.Inject

class SelectedLocationLocalSourceImpl @Inject constructor(@SharedPreferencesModule.Data private val dataPreferences: SharedPreferences) :
        SelectedLocationLocalSource {

    override fun getSelectedLocationId(): String? {

        return dataPreferences.getString(CHOSEN_LOCATION, null)
    }

    override fun select(placeId: String) {

        dataPreferences.edit().putString(CHOSEN_LOCATION, placeId).apply()
    }

    override fun removeSelection() {

        dataPreferences.edit().remove(CHOSEN_LOCATION).apply()
    }

    companion object {

        private const val CHOSEN_LOCATION = "chosen_location"
    }
}