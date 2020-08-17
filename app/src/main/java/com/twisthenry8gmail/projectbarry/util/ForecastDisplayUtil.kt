package com.twisthenry8gmail.projectbarry.util

import android.content.Context
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.data.Temperature

object ForecastDisplayUtil {

    fun displayTemperature(context: Context, temperature: Temperature?): String {

        temperature?.let {

            val formattedValue = DisplayUtil.decimalPlaces(it.value, 0)

            return when (it.scale) {

                Temperature.Scale.CELSIUS, Temperature.Scale.FAHRENHEIT -> context.getString(
                    R.string.temperature_degrees_display,
                    formattedValue
                )

                Temperature.Scale.KELVIN -> formattedValue
            }
        }

        return context.getString(R.string.feature_no_value)
    }

    fun displaySpeed(context: Context, windSpeed: Double): String {

        return context.getString(R.string.speed_display, DisplayUtil.decimalPlaces(windSpeed, 1))
    }
}