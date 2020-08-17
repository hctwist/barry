package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.data.Temperature
import com.twisthenry8gmail.projectbarry.util.DisplayUtil
import com.twisthenry8gmail.projectbarry.util.ForecastDisplayUtil
import com.twisthenry8gmail.projectbarry.util.TimeDisplayUtil
import java.time.ZonedDateTime

abstract class Feature<T> private constructor(
    val titleRes: Int,
    val iconRes: Int,
    protected val value: T
) {

    protected abstract fun displayValue(context: Context, value: T): String

    fun displayValue(context: Context): String {

        return displayValue(context, value)
    }

    class TemperatureLow(value: Temperature) :
        Feature<Temperature>(R.string.temperature_low, R.drawable.feature_temp_low, value) {

        override fun displayValue(context: Context, value: Temperature): String {

            return ForecastDisplayUtil.displayTemperature(context, value)
        }
    }

    class TemperatureHigh(value: Temperature) :
        Feature<Temperature>(R.string.temperature_high, R.drawable.feature_temp_high, value) {

        override fun displayValue(context: Context, value: Temperature): String {

            return ForecastDisplayUtil.displayTemperature(context, value)
        }
    }

    class UVIndex(value: Double) :
        Feature<Double>(R.string.feature_uv_index, R.drawable.feature_uv, value) {

        override fun displayValue(context: Context, value: Double): String {

            return DisplayUtil.decimalPlaces(value, 1)
        }
    }

    class Pop(value: Double) :
        Feature<Double>(R.string.feature_pop, R.drawable.feature_pop, value) {

        override fun displayValue(context: Context, value: Double): String {

            return DisplayUtil.percentage(context, value, 0)
        }
    }

    class FeelsLike(value: Temperature) :
        Feature<Temperature>(
            R.string.feature_feels_like,
            R.drawable.feature_uv,
            value
        ) {

        override fun displayValue(context: Context, value: Temperature): String {

            return ForecastDisplayUtil.displayTemperature(context, value)
        }
    }

    class Humidity(value: Int) :
        Feature<Int>(R.string.feature_humidity, R.drawable.feature_uv, value) {

        override fun displayValue(context: Context, value: Int): String {

            return DisplayUtil.percentage(context, value)
        }
    }

    class WindSpeed(value: Double) :
        Feature<Double>(R.string.feature_wind_speed, R.drawable.feature_uv, value) {

        override fun displayValue(context: Context, value: Double): String {

            return ForecastDisplayUtil.displaySpeed(context, value)
        }
    }

    class Sunrise(time: ZonedDateTime) :
        Feature<ZonedDateTime>(R.string.feature_sunrise, R.drawable.feature_uv, time) {

        override fun displayValue(context: Context, value: ZonedDateTime): String {

            return TimeDisplayUtil.displayTime(value)
        }
    }

    class Sunset(time: ZonedDateTime) :
        Feature<ZonedDateTime>(R.string.feature_sunset, R.drawable.feature_uv, time) {

        override fun displayValue(context: Context, value: ZonedDateTime): String {

            return TimeDisplayUtil.displayTime(value)
        }
    }
}