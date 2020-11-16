package com.twisthenry8gmail.projectbarry.application.view

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.domain.core.WeatherCondition

object WeatherConditionResolver {

    fun resolveImageResource(condition: WeatherCondition): Int? {

        return when (condition) {

            WeatherCondition.CLEAR -> R.drawable.sun

            WeatherCondition.CLOUDS_FEW, WeatherCondition.CLOUDS_SCATTERED -> R.drawable.sun_clouds

            WeatherCondition.CLOUDS_OVERCAST, WeatherCondition.CLOUDS_BROKEN -> R.drawable.sun_clouds

            else -> null
        }
    }

    fun resolveArtResource(condition: WeatherCondition): Int {

        return R.drawable.test_art
    }

    fun resolveArtColor(condition: WeatherCondition): Int {

        return R.color.art_sunny
    }

    fun resolveStringResource(condition: WeatherCondition): Int {

        return when (condition) {

            WeatherCondition.CLEAR -> R.string.condition_clear

            WeatherCondition.CLOUDS_FEW -> R.string.condition_clouds_few

            WeatherCondition.CLOUDS_SCATTERED -> R.string.condition_clouds_scattered

            WeatherCondition.CLOUDS_BROKEN -> R.string.condition_clouds_broken

            WeatherCondition.CLOUDS_OVERCAST -> R.string.condition_clouds_overcast

            WeatherCondition.DRIZZLE_LIGHT -> R.string.condition_drizzle_light

            WeatherCondition.DRIZZLE -> R.string.condition_drizzle

            WeatherCondition.DRIZZLE_HEAVY -> R.string.condition_drizzle_heavy

            WeatherCondition.RAIN_LIGHT -> R.string.condition_rain_light

            WeatherCondition.RAIN -> R.string.condition_rain

            WeatherCondition.RAIN_HEAVY -> R.string.condition_rain_heavy

            WeatherCondition.FREEZING_RAIN -> R.string.condition_freezing_rain

            WeatherCondition.SNOW -> R.string.condition_snow

            WeatherCondition.SNOW_HEAVY -> R.string.condition_snow_heavy

            WeatherCondition.SNOW_RAIN -> R.string.condition_snow_rain

            WeatherCondition.SLEET -> R.string.condition_sleet

            WeatherCondition.THUNDER_DRIZZLE -> R.string.condition_thunder_drizzle

            WeatherCondition.THUNDER_DRIZZLE_HEAVY -> R.string.condition_thunder_drizzle_heavy

            WeatherCondition.THUNDER_RAIN -> R.string.condition_thunder_rain

            WeatherCondition.THUNDER_RAIN_HEAVY -> R.string.condition_thunder_rain_heavy

            WeatherCondition.THUNDER -> R.string.condition_thunder

            WeatherCondition.MIST -> R.string.condition_mist

            WeatherCondition.SMOKE -> R.string.condition_smoke

            WeatherCondition.HAZE -> R.string.condition_haze

            WeatherCondition.FOG -> R.string.condition_fog

            WeatherCondition.DUST -> R.string.condition_dust

            WeatherCondition.SAND -> R.string.condition_sand

            WeatherCondition.ASH -> R.string.condition_ash

            WeatherCondition.SQUALL -> R.string.condition_squall

            WeatherCondition.TORNADO -> R.string.condition_tornado
        }
    }
}