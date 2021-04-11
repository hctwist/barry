package uk.henrytwist.projectbarry.domain.models

class ScaledWindSpeed constructor(val value: Double, val scale: Scale) {

    fun metresPerSecond() = when (scale) {

        Scale.METRES_PER_SECOND -> value
        Scale.MILES_PER_HOUR -> mphToMPS(value)
        Scale.KILOMETRES_PER_HOUR -> kphToMPS(value)
        Scale.KNOTS -> knotsToMPS(value)
    }

    fun milesPerHour() = when (scale) {

        Scale.METRES_PER_SECOND -> mpsToMPH(value)
        Scale.MILES_PER_HOUR -> value
        Scale.KILOMETRES_PER_HOUR -> kphToMPH(value)
        Scale.KNOTS -> mpsToMPH(knotsToMPS(value))
    }

    fun kilometresPerHour() = when (scale) {

        Scale.METRES_PER_SECOND -> mpsToKPH(value)
        Scale.MILES_PER_HOUR -> mphToKPH(value)
        Scale.KILOMETRES_PER_HOUR -> value
        Scale.KNOTS -> mpsToKPH(knotsToMPS(value))
    }

    fun knots() = when (scale) {

        Scale.METRES_PER_SECOND -> mpsToKnots(value)
        Scale.MILES_PER_HOUR -> mpsToKnots(mphToMPS(value))
        Scale.KILOMETRES_PER_HOUR -> mpsToKnots(kphToMPS(value))
        Scale.KNOTS -> value
    }

    fun to(scale: Scale) = ScaledWindSpeed(
            when (scale) {

                Scale.METRES_PER_SECOND -> metresPerSecond()
                Scale.MILES_PER_HOUR -> milesPerHour()
                Scale.KILOMETRES_PER_HOUR -> kilometresPerHour()
                Scale.KNOTS -> knots()
            }, scale
    )

    companion object {

        fun fromMetresPerSecond(value: Double) = ScaledWindSpeed(value, Scale.METRES_PER_SECOND)

        fun mphToMPS(mph: Double) = (mph / 3600) * 1609.344
        fun kphToMPS(kph: Double) = kph / 3.6
        fun knotsToMPS(knots: Double) = knots * (1852.0 / 3600)

        fun mpsToMPH(mps: Double) = (mps / 1609.344) * 3600
        fun kphToMPH(kph: Double) = kph * 1609.344

        fun mpsToKPH(mps: Double) = mps * 3.6
        fun mphToKPH(mph: Double) = mph / 1609.344

        fun mpsToKnots(mps: Double) = mps / (1852.0 / 3600)
    }

    enum class Scale {

        METRES_PER_SECOND, MILES_PER_HOUR, KILOMETRES_PER_HOUR, KNOTS
    }
}