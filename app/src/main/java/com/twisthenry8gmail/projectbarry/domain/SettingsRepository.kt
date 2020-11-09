package com.twisthenry8gmail.projectbarry.domain

import com.twisthenry8gmail.projectbarry.core.ScaledTemperature

interface SettingsRepository {

    fun setTemperatureScale(scale: ScaledTemperature.Scale)

    fun getTemperatureScale(): ScaledTemperature.Scale

    companion object {

        val DEFAULT_TEMPERATURE_SCALE = ScaledTemperature.Scale.CELSIUS
    }
}