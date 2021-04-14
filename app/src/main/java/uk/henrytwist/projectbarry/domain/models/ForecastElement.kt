package uk.henrytwist.projectbarry.domain.models

import uk.henrytwist.kotlinbasics.fractionBetween
import java.time.ZonedDateTime
import kotlin.math.roundToInt

sealed class ForecastElement {

    abstract fun getValue(): Double

    class Temperature(val temperature: ScaledTemperature) : ForecastElement() {

        override fun getValue() = temperature.celsius()

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

        override fun getValue() = index

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

        override fun getValue() = pop

        fun getTag(): Tag {

            return when (pop) {

                // TODO Think about this, maybe add uncertain for 0.3 - 0.5 ish?
                in 0.0..0.2 -> Tag.UNLIKELY
                else -> Tag.LIKELY
            }
        }

        enum class Tag {

            UNLIKELY, LIKELY
        }
    }

    class FeelsLike(val temperature: ScaledTemperature) : ForecastElement() {

        override fun getValue() = temperature.celsius()
    }

    class Humidity(val humidity: Int) : ForecastElement() {

        override fun getValue() = humidity.toDouble()

        fun getTag(): Tag {

            // TODO This is for indoors, not out. Maybe consider dew point instead?
            return when (humidity) {

                in 0..24 -> Tag.LOW
                in 25..29 -> Tag.FAIR
                in 30..59 -> Tag.HEALTHY
                in 60..69 -> Tag.FAIR
                else -> Tag.HIGH
            }
        }

        enum class Tag {

            LOW, FAIR, HEALTHY, HIGH
        }
    }

    class WindSpeed(val speed: ScaledWindSpeed) : ForecastElement() {

        override fun getValue() = speed.metresPerSecond()

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