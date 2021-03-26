package uk.henrytwist.projectbarry.domain.data

import uk.henrytwist.projectbarry.domain.models.ScaledTemperature

interface SettingsRepository {

    fun setTemperatureScale(scale: ScaledTemperature.Scale)

    fun getTemperatureScale(): ScaledTemperature.Scale

    companion object {

        val DEFAULT_TEMPERATURE_SCALE = ScaledTemperature.Scale.CELSIUS
    }
}