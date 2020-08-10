package com.twisthenry8gmail.projectbarry.view

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.data.WeatherCondition

object WeatherConditionDisplay {

    fun getImageResource(condition: WeatherCondition, featureIcon: Boolean): Int? {

        return when (condition) {

            WeatherCondition.CLEAR -> if (featureIcon) R.drawable.sun else R.drawable.sun_standard

            WeatherCondition.CLOUDS_OVERCAST, WeatherCondition.CLOUDS_BROKEN -> if (featureIcon) R.drawable.cloud else R.drawable.cloud_standard

            WeatherCondition.CLOUDS_FEW, WeatherCondition.CLOUDS_SCATTERED -> if (featureIcon) R.drawable.few_clouds else null

            else -> null
        }
    }
}