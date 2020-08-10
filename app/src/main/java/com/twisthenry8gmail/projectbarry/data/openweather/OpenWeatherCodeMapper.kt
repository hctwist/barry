package com.twisthenry8gmail.projectbarry.data.openweather

import com.twisthenry8gmail.projectbarry.data.WeatherCondition
import com.twisthenry8gmail.projectbarry.data.WeatherCondition.*

object OpenWeatherCodeMapper {

    fun map(code: Int): WeatherCondition {

        return when (code) {

            200 -> THUNDER_RAIN_LIGHT
            201 -> THUNDER_RAIN
            202 -> THUNDER_RAIN_HEAVY
            210, 211, 212, 221 -> THUNDER
            230 -> THUNDER_DRIZZLE_LIGHT
            231 -> THUNDER_DRIZZLE
            232 -> THUNDER_DRIZZLE_HEAVY

            300, 310 -> DRIZZLE_LIGHT
            301, 311, 321 -> DRIZZLE
            302, 312 -> DRIZZLE_HEAVY

            314, 502, 503, 504, 522 -> RAIN_HEAVY
            313, 501, 521, 531 -> RAIN
            500, 520 -> RAIN_LIGHT

            600, 601, 620, 621 -> SNOW
            602, 622 -> SNOW_HEAVY
            615 -> SNOW_RAIN_LIGHT
            616 -> SNOW_RAIN

            800 -> CLEAR
            801 -> CLOUDS_FEW
            802 -> CLOUDS_SCATTERED
            803 -> CLOUDS_BROKEN
            804 -> CLOUDS_OVERCAST

            // TODO 511, 611, 612, 613, 7xx

            else -> NOTHING
        }
    }
}