package uk.henrytwist.projectbarry.application.view.onboarding

import android.content.Context
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature

object OnboardingResolver {

    fun resolveSimpleTemperatureScale(context: Context, scale: ScaledTemperature.Scale): String {

        return context.resources.getStringArray(R.array.settings_temperature_scale_simple_entries)[scale.ordinal]
    }

    fun resolveSimpleSpeedScale(context: Context, scale: ScaledSpeed.Scale): String {

        return context.resources.getStringArray(R.array.settings_speed_scale_simple_entries)[scale.ordinal]
    }
}