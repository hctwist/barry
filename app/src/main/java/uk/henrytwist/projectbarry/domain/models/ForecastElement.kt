package uk.henrytwist.projectbarry.domain.models

import kotlin.math.roundToInt

sealed class ForecastElement {

    class Temperature(val temperature: ScaledTemperature) : ForecastElement() {

        fun getTag(): Tag {

            return when (temperature.celsius()) {

                in -Double.MAX_VALUE..0.0 -> Tag.FREEZING
                in 0.0..10.0 -> Tag.CHILLY
                in 10.0..19.0 -> Tag.MODERATE
                in 19.0..25.0 -> Tag.WARM
                else -> Tag.HOT
            }
        }

        enum class Tag {

            FREEZING, CHILLY, MODERATE, WARM, HOT
        }
    }

    class UVIndex(val index: Double) : ForecastElement() {

        fun getTag(): Tag {

            if (index == 0.0) return Tag.ZERO

            return when (index.roundToInt()) {

                in 0..2 -> Tag.LOW
                in 3..5 -> Tag.MODERATE
                in 6..7 -> Tag.HIGH
                in 8..10 -> Tag.VERY_HIGH
                else -> Tag.EXTREMELY_HIGH
            }
        }

        enum class Tag {

            ZERO, LOW, MODERATE, HIGH, VERY_HIGH, EXTREMELY_HIGH
        }
    }

    class Pop(val pop: Double) : ForecastElement() {

        fun getTag(): Tag {

            return when (pop) {

                in 0.8..1.0 -> Tag.VERY_LIKELY
                in 0.4..0.8 -> Tag.LIKELY
                in 0.2..0.4 -> Tag.POSSIBLE
                in 0.1..0.2 -> Tag.UNLIKELY
                else -> Tag.NONE
            }
        }

        enum class Tag {

            NONE, UNLIKELY, POSSIBLE, LIKELY, VERY_LIKELY
        }
    }

    class FeelsLike(val temperature: ScaledTemperature) : ForecastElement()

    class DewPoint(val dewPoint: ScaledTemperature) : ForecastElement() {

        fun getTag(): Tag {

            return when (dewPoint.fahrenheit()) {

                in 0.0..55.0 -> Tag.COMFORTABLE
                in 55.0..65.0 -> Tag.MUGGY
                else -> Tag.UNCOMFORTABLE
            }
        }

        enum class Tag {

            COMFORTABLE, MUGGY, UNCOMFORTABLE
        }
    }

    class WindSpeed(val speed: ScaledSpeed) : ForecastElement() {

        fun getTag(): Tag {

            return when (speed.metresPerSecond()) {

                in 0.0..1.5 -> Tag.CALM
                in 1.5..5.5 -> Tag.GENTLE
                in 5.5..10.7 -> Tag.MODERATE
                in 10.7..17.1 -> Tag.STRONG
                in 17.1..24.4 -> Tag.GALE
                else -> Tag.DANGEROUS
            }
        }

        enum class Tag {

            CALM, GENTLE, MODERATE, STRONG, GALE, DANGEROUS
        }
    }
}