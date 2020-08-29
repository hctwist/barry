package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.core.ForecastDisplayUtil
import kotlin.math.roundToInt

@Deprecated("Replaced by ForecastElement")
abstract class HourlyFeature<T>(protected val value: T) {

    abstract fun displayValue(context: Context): String

    abstract fun toDouble(): Double

    class ForecastedTemperature(value: ScaledTemperature) : HourlyFeature<ScaledTemperature>(value) {

        override fun displayValue(context: Context): String {

            return ForecastDisplayUtil.displayTemperature(context, value)
        }

        override fun toDouble(): Double {

            return value.value.roundToInt().toDouble()
        }
    }

    class Pop(value: Double) : HourlyFeature<Double>(value) {

        override fun displayValue(context: Context): String {

            return ForecastDisplayUtil.displayPop(context, value)
        }

        override fun toDouble(): Double {

            return value
        }
    }
}