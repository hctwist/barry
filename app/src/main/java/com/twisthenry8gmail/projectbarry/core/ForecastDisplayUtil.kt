package com.twisthenry8gmail.projectbarry.core

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import com.twisthenry8gmail.projectbarry.R
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

    fun displayPop(context: Context, pop: Double): String {

        return DisplayUtil.percentage(context, pop, 0)
    }

    fun displayHumidity(context: Context, humidity: Int): String {

        return DisplayUtil.percentage(context, humidity)
    }

    fun getElementDisplayString(context: Context, element: ForecastElement): String {

        return when(element) {

            is ForecastElement.Temperature -> displayTemperature(context, element.temperature)
            is ForecastElement.UVIndex -> "" // TODO
            is ForecastElement.Pop -> ""
            is ForecastElement.FeelsLike -> ""
            is ForecastElement.Humidity -> ""
            is ForecastElement.WindSpeed -> ""
        }
    }

    fun getElementTitle(context: Context, element: ForecastElement): String {

        return when(element) {

            is ForecastElement.Temperature -> displayTemperature(context, element.temperature)
            is ForecastElement.UVIndex -> "" // TODO
            is ForecastElement.Pop -> ""
            is ForecastElement.FeelsLike -> ""
            is ForecastElement.Humidity -> ""
            is ForecastElement.WindSpeed -> ""
        }
    }

    fun getElementIcon(context: Context, element: ForecastElement): Drawable {

        val nothing = ShapeDrawable() // TODO
        return when(element) {

            is ForecastElement.Temperature -> nothing
            is ForecastElement.UVIndex -> nothing
            is ForecastElement.Pop -> nothing
            is ForecastElement.FeelsLike -> nothing
            is ForecastElement.Humidity -> nothing
            is ForecastElement.WindSpeed -> nothing
        }
    }
}