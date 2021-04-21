package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.models.ForecastElement

object ForecastElementResolver {

    fun getElementDisplayString(context: Context, element: ForecastElement): String {

        return when (element) {

            is ForecastElement.Temperature -> ForecastResolver.displayTemperature(context, element.temperature)!!
            is ForecastElement.UVIndex -> ForecastResolver.displayUVIndex(element.index)
            is ForecastElement.Pop -> ForecastResolver.displayPop(context, element.pop)
            is ForecastElement.FeelsLike -> ForecastResolver.displayTemperature(context, element.temperature)!!
            is ForecastElement.DewPoint -> ForecastResolver.displayTemperature(context, element.dewPoint)!!
            is ForecastElement.WindSpeed -> ForecastResolver.displaySpeed(context, element.speed)
        }
    }

    fun getElementTitle(context: Context, element: ForecastElement): String {

        return context.getString(
                when (element) {

                    is ForecastElement.Temperature -> R.string.element_temperature
                    is ForecastElement.UVIndex -> R.string.element_uv_index
                    is ForecastElement.Pop -> R.string.element_pop
                    is ForecastElement.FeelsLike -> R.string.element_feels_like
                    is ForecastElement.DewPoint -> R.string.element_dew_point
                    is ForecastElement.WindSpeed -> R.string.element_wind_speed
                }
        )
    }

    fun getElementIcon(context: Context, element: ForecastElement): Drawable? {

        return when (element) {

            is ForecastElement.UVIndex -> R.drawable.standard_beach_ball
            is ForecastElement.Pop -> R.drawable.standard_umbrella
            is ForecastElement.FeelsLike -> R.drawable.standard_jacket
            is ForecastElement.DewPoint -> R.drawable.standard_droplet
            is ForecastElement.WindSpeed -> R.drawable.standard_wind_turbine
            else -> null
        }?.let {

            ContextCompat.getDrawable(context, it)
        }
    }
}