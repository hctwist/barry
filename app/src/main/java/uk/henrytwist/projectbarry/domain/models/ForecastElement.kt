package uk.henrytwist.projectbarry.domain.models

sealed class ForecastElement {

    internal abstract val tagRange: TagRange

    abstract fun getValue(): Double

    fun getTag(): Tag {

        return tagRange.query(getValue())
    }

    class Temperature(val temperature: ScaledTemperature) : ForecastElement() {

        override val tagRange = TagRange(
                Tag.TEMP_FREEZING,
                Tag.TEMP_CHILLY from 0.0,
                Tag.TEMP_MODERATE from 10.0,
                Tag.TEMP_WARM from 19.0,
                Tag.TEMP_HOT from 25.0
        )

        override fun getValue(): Double {

            return temperature.celsius()
        }
    }

    class UVIndex(val index: Double) : ForecastElement() {

        override val tagRange = TagRange(
                Tag.UV_ZERO,
                Tag.UV_LOW from 0.0,
                Tag.UV_MODERATE from 2.5,
                Tag.UV_HIGH from 5.5,
                Tag.UV_VERY_HIGH from 7.5,
                Tag.UV_EXTREMELY_HIGH from 10.5
        )

        override fun getValue(): Double {

            return index
        }
    }

    class Pop(val pop: Double) : ForecastElement() {

        override val tagRange = TagRange(
                Tag.POP_NONE,
                Tag.POP_UNLIKELY from 0.1,
                Tag.POP_POSSIBLE from 0.2,
                Tag.POP_LIKELY from 0.45,
                Tag.POP_VERY_LIKELY from 0.8
        )

        override fun getValue(): Double {

            return pop
        }
    }

    class FeelsLike(val temperature: ScaledTemperature) : ForecastElement() {

        private val backingElement = Temperature(temperature)

        override val tagRange = backingElement.tagRange

        override fun getValue(): Double {

            return backingElement.getValue()
        }
    }

    class DewPoint(val dewPoint: ScaledTemperature) : ForecastElement() {

        override val tagRange = TagRange(
                Tag.DEW_POINT_COMFORTABLE,
                Tag.DEW_POINT_MUGGY from 55.0,
                Tag.DEW_POINT_UNCOMFORTABLE from 65.0
        )

        override fun getValue(): Double {

            return dewPoint.fahrenheit()
        }
    }

    class WindSpeed(val speed: ScaledSpeed) : ForecastElement() {

        override val tagRange = TagRange(
                Tag.SPEED_CALM,
                Tag.SPEED_GENTLE from 1.5,
                Tag.SPEED_MODERATE from 5.5,
                Tag.SPEED_STRONG from 10.7,
                Tag.SPEED_GALE from 17.1,
                Tag.SPEED_DANGEROUS from 24.4
        )

        override fun getValue(): Double {

            return speed.metresPerSecond()
        }
    }

    class TagRange(internal vararg val intervals: From) {

        class From(val value: Double, val tag: Tag)

        fun query(value: Double): Tag {

            for (i in intervals.indices.reversed()) {

                if (value >= intervals[i].value) return intervals[i].tag
            }

            return intervals.first().tag
        }
    }

    infix fun Tag.from(value: Double): TagRange.From {

        return TagRange.From(value, this)
    }

    enum class Tag {

        TEMP_FREEZING, TEMP_CHILLY, TEMP_MODERATE, TEMP_WARM, TEMP_HOT,
        UV_ZERO, UV_LOW, UV_MODERATE, UV_HIGH, UV_VERY_HIGH, UV_EXTREMELY_HIGH,
        POP_NONE, POP_UNLIKELY, POP_POSSIBLE, POP_LIKELY, POP_VERY_LIKELY,
        DEW_POINT_COMFORTABLE, DEW_POINT_MUGGY, DEW_POINT_UNCOMFORTABLE,
        SPEED_CALM, SPEED_GENTLE, SPEED_MODERATE, SPEED_STRONG, SPEED_GALE, SPEED_DANGEROUS,
    }
}