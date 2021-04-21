package uk.henrytwist.projectbarry.application.data.settings

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.di.SharedPreferencesModule
import uk.henrytwist.projectbarry.application.view.settings.IntegerBackedListPreference
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.SettingsRepository.Companion.DEFAULT_TEMPERATURE_SCALE
import uk.henrytwist.projectbarry.domain.data.SettingsRepository.Companion.DEFAULT_WIND_SPEED
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
        private val resources: Resources,
        @SharedPreferencesModule.Settings private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    fun getDarkMode(): Int {

        return getDarkMode(resources, sharedPreferences)
    }

    override fun setTemperatureScale(scale: ScaledTemperature.Scale) {

        sharedPreferences.edit().putInt(key(R.string.settings_temperature_scale_key), scale.ordinal).apply()
    }

    override fun getTemperatureScale(): ScaledTemperature.Scale {

        return ScaledTemperature.Scale.values()[IntegerBackedListPreference.get(
                sharedPreferences,
                key(R.string.settings_temperature_scale_key),
                DEFAULT_TEMPERATURE_SCALE.ordinal
        )]
    }

    override fun getWindSpeedScale(): ScaledSpeed.Scale {

        return ScaledSpeed.Scale.values()[IntegerBackedListPreference.get(
                sharedPreferences,
                key(R.string.settings_wind_speed_scale_key),
                DEFAULT_WIND_SPEED.ordinal
        )]
    }

    private fun key(@StringRes keyRes: Int) = resources.getString(keyRes)

    companion object {

        fun getDarkMode(resources: Resources, sharedPreferences: SharedPreferences): Int {

            return sharedPreferences.getString(resources.getString(R.string.settings_dark_mode_key), null)?.toInt()?.let { value ->

                when (value) {

                    0 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    1 -> AppCompatDelegate.MODE_NIGHT_NO
                    2 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> null
                }
            } ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
}