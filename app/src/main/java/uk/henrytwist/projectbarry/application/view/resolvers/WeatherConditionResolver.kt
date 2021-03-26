package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.models.WeatherCondition

object WeatherConditionResolver {

    fun resolveIcon(context: Context, condition: WeatherCondition?): Drawable? {

        if (condition == null) return null

        return ContextCompat.getDrawable(context, when (condition) {

            WeatherCondition.CLEAR -> R.drawable.icon_sun
            WeatherCondition.CLOUDS_FEW, WeatherCondition.CLOUDS_SCATTERED -> R.drawable.icon_partly_cloudy
            WeatherCondition.CLOUDS_OVERCAST, WeatherCondition.CLOUDS_BROKEN -> R.drawable.icon_cloudy
            WeatherCondition.DRIZZLE_LIGHT, WeatherCondition.RAIN_LIGHT -> R.drawable.icon_light_rain
            WeatherCondition.DRIZZLE, WeatherCondition.DRIZZLE_HEAVY, WeatherCondition.RAIN, WeatherCondition.RAIN_HEAVY -> R.drawable.icon_rain
            WeatherCondition.FREEZING_RAIN, WeatherCondition.SNOW_RAIN, WeatherCondition.SLEET -> R.drawable.icon_sleet
            WeatherCondition.SNOW, WeatherCondition.SNOW_HEAVY -> R.drawable.icon_snow
            WeatherCondition.THUNDER, WeatherCondition.THUNDER_DRIZZLE, WeatherCondition.THUNDER_DRIZZLE_HEAVY, WeatherCondition.THUNDER_RAIN, WeatherCondition.THUNDER_RAIN_HEAVY -> R.drawable.icon_storm
            WeatherCondition.MIST, WeatherCondition.SMOKE, WeatherCondition.HAZE, WeatherCondition.FOG, WeatherCondition.SQUALL, WeatherCondition.TORNADO -> R.drawable.icon_haze
            WeatherCondition.DUST, WeatherCondition.SAND, WeatherCondition.ASH -> R.drawable.icon_dust
        })
    }

    fun resolveArt(context: Context, condition: WeatherCondition?): Drawable? {

        if (condition == null) return null

        val art = listOf(R.drawable.cloudy_art, R.drawable.clear_night_art, R.drawable.sunny_art)
        return ContextCompat.getDrawable(context, art.random())
    }

    fun resolveName(context: Context, condition: WeatherCondition?): String? {

        return condition?.let {

            context.getString(resolveNameStringResource(condition))
        }
    }

    fun resolveSimpleName(context: Context, condition: WeatherCondition): String {

        return context.getString(when (condition.group) {

            WeatherCondition.Group.CLEAR -> if (condition == WeatherCondition.CLEAR) R.string.condition_clear else R.string.condition_type_cloudy

            WeatherCondition.Group.RAIN -> R.string.condition_rain

            WeatherCondition.Group.THUNDER -> R.string.condition_thunder

            // TODO Should include sleet? But it might say "sleet all day" even if it changes to snow soon
            WeatherCondition.Group.SNOW -> R.string.condition_snow

            WeatherCondition.Group.VISIBILITY, WeatherCondition.Group.PARTICLES, WeatherCondition.Group.EXTREME_WIND -> resolveNameStringResource(condition)
        })
    }

    private fun resolveNameStringResource(condition: WeatherCondition): Int {

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