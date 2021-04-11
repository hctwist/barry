package uk.henrytwist.projectbarry.application.view.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.data.settings.SettingsRepositoryImpl
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var settingsRepository: SettingsRepositoryImpl

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onResume() {
        super.onResume()

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        when (key) {

            getString(R.string.settings_dark_mode_key) -> {

                AppCompatDelegate.setDefaultNightMode(settingsRepository.getDarkMode())
            }
        }
    }
}