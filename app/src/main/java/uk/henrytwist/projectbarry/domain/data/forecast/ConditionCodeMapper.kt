package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import uk.henrytwist.projectbarry.domain.models.WeatherCondition.*

object ConditionCodeMapper {

    fun map(code: Int): WeatherCondition {

        return when (code) {

            200, 201 -> THUNDER_RAIN
            202 -> THUNDER_RAIN_HEAVY
            210, 211, 212, 221 -> THUNDER
            230, 231 -> THUNDER_DRIZZLE
            232 -> THUNDER_DRIZZLE_HEAVY

            300, 310 -> DRIZZLE_LIGHT
            301, 311, 321 -> DRIZZLE
            302, 312 -> DRIZZLE_HEAVY

            314, 502, 503, 504, 522 -> RAIN_HEAVY
            313, 501, 521, 531 -> RAIN
            500, 520 -> RAIN_LIGHT
            511 -> FREEZING_RAIN

            600, 601, 620, 621 -> SNOW
            602, 622 -> SNOW_HEAVY
            611, 612, 613 -> SLEET
            615, 616 -> SNOW_RAIN

            701 -> MIST
            711 -> SMOKE
            721 -> HAZE
            731, 761 -> DUST
            741 -> FOG
            751 -> SAND
            762 -> ASH
            771 -> SQUALL
            781 -> TORNADO

            800 -> CLEAR
            801 -> CLOUDS_FEW
            802 -> CLOUDS_SCATTERED
            803 -> CLOUDS_BROKEN
            804 -> CLOUDS_OVERCAST

            else -> throw IllegalArgumentException("Open weather code not recognised")
        }
    }
}