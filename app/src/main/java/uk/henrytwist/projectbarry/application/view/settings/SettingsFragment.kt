package uk.henrytwist.projectbarry.application.view.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        listView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        listView.isNestedScrollingEnabled = false
    }

    override fun onResume() {

        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {

        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {

        when (preference?.key) {

            getString(R.string.settings_about_key) -> findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
        }

        return super.onPreferenceTreeClick(preference)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        when (key) {

            getString(R.string.settings_dark_mode_key) -> {

                AppCompatDelegate.setDefaultNightMode(settingsRepository.getDarkMode())
            }
        }
    }
}