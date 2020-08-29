package com.twisthenry8gmail.projectbarry.data

import android.content.SharedPreferences
import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import javax.inject.Inject

class SettingsRepository @Inject constructor(@SharedPreferencesModule.Settings val sharedPreferences: SharedPreferences) {

    fun setRefreshInterval(interval: Long) {

        sharedPreferences.edit().putLong(KEY_REFRESH_INTERVAL, interval).apply()
    }

    fun getRefreshInterval(): Long {

        return sharedPreferences.getLong(KEY_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL)
    }

    fun setTemperatureScale(scale: ScaledTemperature.Scale) {

        sharedPreferences.edit().putInt(KEY_TEMPERATURE_SCALE, scale.ordinal).apply()
    }

    fun getTemperatureScale(): ScaledTemperature.Scale {

        return ScaledTemperature.Scale.values()[sharedPreferences.getInt(
            KEY_TEMPERATURE_SCALE,
            DEFAULT_TEMPERATURE_SCALE.ordinal
        )]
    }

    companion object {

        const val DEFAULT_REFRESH_INTERVAL = 60 * 60L
        const val KEY_REFRESH_INTERVAL = "refresh_interval"

        val DEFAULT_TEMPERATURE_SCALE = ScaledTemperature.Scale.CELSIUS
        const val KEY_TEMPERATURE_SCALE = "temperature_scale"
    }
}