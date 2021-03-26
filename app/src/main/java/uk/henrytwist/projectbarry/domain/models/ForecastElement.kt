package uk.henrytwist.projectbarry.domain.models

import uk.henrytwist.kotlinbasics.fractionBetween
import java.time.ZonedDateTime
import kotlin.math.roundToInt

sealed class ForecastElement {

    abstract fun getValue(): Double

    abstract fun getSeverity(): Double

    class Temperature(val temperature: ScaledTemperature) : ForecastElement() {

        override fun getValue() = temperature.celsius()

        override fun getSeverity(): Double {

            return computeSeverity(temperature.celsius(), 8.0, -5.0, 15.0, 30.0)
        }
    }

    class UVIndex(val index: Double) : ForecastElement() {

        override fun getValue() = index

        override fun getSeverity(): Double {
            TODO("Not yet implemented")
        }

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

        override fun getSeverity(): Double {

            return computeSeverity(pop, 0.25, 1.0)
        }

        fun getTag(): Tag {

            return when (pop) {

                // TODO Think about this
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

        override fun getSeverity(): Double {
            TODO("Not yet implemented")
        }
    }

    class Humidity(val humidity: Int) : ForecastElement() {

        override fun getValue() = humidity.toDouble()

        override fun getSeverity(): Double {
            TODO("Not yet implemented")
        }

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

    class WindSpeed(val speed: Double) : ForecastElement() {

        override fun getValue() = speed

        override fun getSeverity(): Double {
            TODO("Not yet implemented")
        }

        fun getTag(): Tag {

            return when (speed) {

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

    class Sunset(val time: ZonedDateTime) : ForecastElement() {

        override fun getValue() = throw elementValueException()

        override fun getSeverity(): Double {
            TODO("Not yet implemented")
        }
    }

    class Sunrise(val time: ZonedDateTime) : ForecastElement() {

        override fun getValue() = throw elementValueException()

        override fun getSeverity(): Double {
            TODO("Not yet implemented")
        }
    }

    protected fun elementValueException() = RuntimeException("This element does not have a value")

    protected fun computeSeverity(value: Double, highThreshold: Double, highMax: Double): Double {

        return fractionBetween(value, highThreshold, highMax)
    }

    protected fun computeSeverity(value: Double, lowThreshold: Double, lowMin: Double, highThreshold: Double, highMax: Double): Double {

        return when {

            value <= lowThreshold -> {

                -fractionBetween(value, lowThreshold, lowMin)
            }
            value > highThreshold -> {

                fractionBetween(value, highThreshold, highMax)
            }
            else -> 0.0
        }
    }
}