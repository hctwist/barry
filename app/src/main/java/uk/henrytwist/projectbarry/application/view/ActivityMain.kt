package uk.henrytwist.projectbarry.application.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.data.settings.SettingsRepositoryImpl

@AndroidEntryPoint
class ActivityMain : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(
                SettingsRepositoryImpl.getDarkMode(resources, PreferenceManager.getDefaultSharedPreferences(this))
        )

        super.onCreate(savedInstanceState)
    }
}