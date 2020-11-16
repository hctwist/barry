package com.twisthenry8gmail.projectbarry.application.data.settings

import android.content.SharedPreferences
import com.twisthenry8gmail.projectbarry.application.data.SharedPreferencesModule
import com.twisthenry8gmail.projectbarry.domain.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.domain.SettingsRepository
import com.twisthenry8gmail.projectbarry.domain.SettingsRepository.Companion.DEFAULT_TEMPERATURE_SCALE
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(@SharedPreferencesModule.Settings private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun setTemperatureScale(scale: ScaledTemperature.Scale) {

        sharedPreferences.edit().putInt(KEY_TEMPERATURE_SCALE, scale.ordinal).apply()
    }

    override fun getTemperatureScale(): ScaledTemperature.Scale {

        return ScaledTemperature.Scale.values()[sharedPreferences.getInt(
            KEY_TEMPERATURE_SCALE,
            DEFAULT_TEMPERATURE_SCALE.ordinal
        )]
    }

    companion object {

        const val KEY_TEMPERATURE_SCALE = "temperature_scale"
    }
}