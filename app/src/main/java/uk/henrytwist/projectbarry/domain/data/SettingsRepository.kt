package uk.henrytwist.projectbarry.domain.data

import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed

interface SettingsRepository {

    fun setTemperatureScale(scale: ScaledTemperature.Scale)

    fun getTemperatureScale(): ScaledTemperature.Scale

    fun setSpeedScale(scale: ScaledSpeed.Scale)

    fun getSpeedScale(): ScaledSpeed.Scale

    companion object {

        val DEFAULT_TEMPERATURE_SCALE = ScaledTemperature.Scale.CELSIUS
        val DEFAULT_SPEED_SCALE = ScaledSpeed.Scale.METRES_PER_SECOND
    }
}