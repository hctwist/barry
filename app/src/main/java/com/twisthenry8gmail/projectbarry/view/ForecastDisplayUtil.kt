package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.ForecastElement
import com.twisthenry8gmail.projectbarry.core.ScaledTemperature
import com.twisthenry8gmail.projectbarry.util.DisplayUtil

object ForecastDisplayUtil {

    fun displayTemperature(context: Context, temperature: ScaledTemperature): String {

        val formattedValue = DisplayUtil.decimalPlaces(temperature.value, 0)

        return when (temperature.scale) {

            ScaledTemperature.Scale.CELSIUS, ScaledTemperature.Scale.FAHRENHEIT -> context.getString(
                R.string.temperature_degrees_display,
                formattedValue
            )

            ScaledTemperature.Scale.KELVIN -> formattedValue
        }
    }

    fun displaySpeed(context: Context, windSpeed: Double): String {

        return context.getString(R.string.speed_display, DisplayUtil.decimalPlaces(windSpeed, 1))
    }

    fun getElementDisplayString(context: Context, element: ForecastElement): String {

        return when (element) {

            is ForecastElement.Temperature -> displayTemperature(context, element.temperature)
            is ForecastElement.UVIndex -> DisplayUtil.decimalPlaces(element.index, 1)
            is ForecastElement.Pop -> DisplayUtil.percentage(context, element.pop, 0)
            is ForecastElement.FeelsLike -> displayTemperature(context, element.temperature)
            is ForecastElement.Humidity -> DisplayUtil.percentage(context, element.humidity)
            is ForecastElement.WindSpeed -> displaySpeed(context, element.speed)
        }
    }

    fun getElementTitle(context: Context, element: ForecastElement): String {

        return context.getString(
            when (element) {

                is ForecastElement.Temperature -> R.string.element_temperature
                is ForecastElement.UVIndex -> R.string.element_uv_index
                is ForecastElement.Pop -> R.string.element_pop
                is ForecastElement.FeelsLike -> R.string.element_feels_like
                is ForecastElement.Humidity -> R.string.element_humidity
                is ForecastElement.WindSpeed -> R.string.element_wind_speed
            }
        )
    }

    fun getElementIcon(context: Context, element: ForecastElement): Drawable? {

        return when (element) {

            is ForecastElement.UVIndex -> R.drawable.feature_uv
            is ForecastElement.Pop -> R.drawable.feature_pop
            else -> null
        }?.let {

            ContextCompat.getDrawable(context, it)
        }
    }
}