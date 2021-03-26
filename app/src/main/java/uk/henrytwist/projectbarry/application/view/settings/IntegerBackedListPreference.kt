package uk.henrytwist.projectbarry.application.view.settings

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.preference.ListPreference

class IntegerBackedListPreference(context: Context, attrs: AttributeSet) :
        ListPreference(context, attrs) {

    init {

        val entryValues = Array(entries.size) {

            it.toString()
        }
        setEntryValues(entryValues)

        setDefaultValue("0")
    }

    companion object {

        fun get(preferences: SharedPreferences, key: String, defaultValue: Int): Int {

            return preferences.getString(key, null)?.toInt() ?: defaultValue
        }
    }
}