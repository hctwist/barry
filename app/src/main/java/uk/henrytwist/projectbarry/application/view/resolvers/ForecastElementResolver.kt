package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.TimeDisplayUtil
import uk.henrytwist.projectbarry.application.view.components.ElementTag
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.util.DisplayUtil

// TODO Move functions from ForecastResolver here
object ForecastElementResolver {

    fun getElementDisplayString(context: Context, element: ForecastElement): String {

        return when (element) {

            is ForecastElement.Temperature -> ForecastResolver.displayTemperature(context, element.temperature)!!
            is ForecastElement.UVIndex -> ForecastResolver.displayUVIndex(element.index)
            is ForecastElement.Pop -> ForecastResolver.displayPop(context, element.pop)
            is ForecastElement.FeelsLike -> ForecastResolver.displayTemperature(context, element.temperature)!!
            is ForecastElement.Humidity -> DisplayUtil.percentage(context, element.humidity)
            is ForecastElement.WindSpeed -> ForecastResolver.displaySpeed(context, element.speed)
            is ForecastElement.Sunrise -> TimeDisplayUtil.displayTime(element.time)
            is ForecastElement.Sunset -> TimeDisplayUtil.displayTime(element.time)
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
                    is ForecastElement.Sunrise -> R.string.element_sunrise
                    is ForecastElement.Sunset -> R.string.element_sunset
                }
        )
    }

    fun getElementIcon(context: Context, element: ForecastElement): Drawable? {

        return when (element) {

            is ForecastElement.UVIndex -> R.drawable.standard_beach_ball
            is ForecastElement.Pop -> R.drawable.standard_umbrella
            is ForecastElement.FeelsLike -> R.drawable.standard_jacket
            is ForecastElement.Humidity -> R.drawable.standard_droplet
            is ForecastElement.WindSpeed -> R.drawable.standard_wind_turbine
            is ForecastElement.Sunrise -> R.drawable.standard_bird
            is ForecastElement.Sunset -> R.drawable.standard_owl
            else -> null
        }?.let {

            ContextCompat.getDrawable(context, it)
        }
    }

    fun resolveValue(forecastElement: ForecastElement): Double {

        return forecastElement.getValue()
    }

    fun resolveSeverityColor(context: Context, element: ForecastElement): Int {

        val baseColor = ContextCompat.getColor(context, R.color.severity_base)

        val highColor = when (element) {

            is ForecastElement.Pop -> ContextCompat.getColor(context, R.color.pop_severe)
            is ForecastElement.Temperature -> ContextCompat.getColor(context, R.color.temperature_severe_high)
            else -> null
        }

        val lowColor = when (element) {

            is ForecastElement.Temperature -> ContextCompat.getColor(context, R.color.temperature_severe_low)
            else -> null
        }

        val severity = element.getSeverity()

        return if (severity > 0 && highColor != null) {

            ColorUtils.blendARGB(baseColor, highColor, severity.toFloat())
        } else if (severity < 0 && lowColor != null) {

            ColorUtils.blendARGB(baseColor, lowColor, -severity.toFloat())
        } else {

            baseColor
        }
    }

    fun resolveTag(forecastElement: ForecastElement): ElementTag.Tag? {

        return when (forecastElement) {

            is ForecastElement.UVIndex -> resolveTag(forecastElement)
            is ForecastElement.Pop -> resolveTag(forecastElement)
            is ForecastElement.Humidity -> resolveTag(forecastElement)
            is ForecastElement.WindSpeed -> resolveTag(forecastElement)
            else -> null
        }
    }

    private fun resolveTag(uvElement: ForecastElement.UVIndex): ElementTag.Tag {

        return when (uvElement.getTag()) {

            ForecastElement.UVIndex.Tag.ZERO -> elementTag(R.string.uv_tag_zero, R.color.uv_tag_zero)
            ForecastElement.UVIndex.Tag.LOW -> elementTag(R.string.uv_tag_low, R.color.uv_tag_low)
            ForecastElement.UVIndex.Tag.MODERATE -> elementTag(R.string.uv_tag_moderate, R.color.uv_tag_moderate)
            ForecastElement.UVIndex.Tag.HIGH -> elementTag(R.string.uv_tag_high, R.color.uv_tag_high)
            ForecastElement.UVIndex.Tag.VERY_HIGH -> elementTag(R.string.uv_tag_very_high, R.color.uv_tag_very_high)
            ForecastElement.UVIndex.Tag.EXTREMELY_HIGH -> elementTag(R.string.uv_tag_extremely_high, R.color.uv_tag_extremely_high)
        }
    }

    private fun resolveTag(popElement: ForecastElement.Pop): ElementTag.Tag {

        return when (popElement.getTag()) {

            ForecastElement.Pop.Tag.UNLIKELY -> elementTag(R.string.pop_tag_unlikely, R.color.pop_tag_unlikely)
            ForecastElement.Pop.Tag.LIKELY -> elementTag(R.string.pop_tag_likely, R.color.pop_tag_likely)
        }
    }

    private fun resolveTag(humidityElement: ForecastElement.Humidity): ElementTag.Tag {

        return when (humidityElement.getTag()) {

            ForecastElement.Humidity.Tag.LOW -> elementTag(R.string.humidity_tag_low, R.color.humidity_tag_low)
            ForecastElement.Humidity.Tag.FAIR -> elementTag(R.string.humidity_tag_fair, R.color.humidity_tag_fair)
            ForecastElement.Humidity.Tag.HEALTHY -> elementTag(R.string.humidity_tag_healthy, R.color.humidity_tag_healthy)
            ForecastElement.Humidity.Tag.HIGH -> elementTag(R.string.humidity_tag_high, R.color.humidity_tag_high)
        }
    }

    private fun resolveTag(windSpeedElement: ForecastElement.WindSpeed): ElementTag.Tag {

        return when (windSpeedElement.getTag()) {

            ForecastElement.WindSpeed.Tag.CALM -> elementTag(R.string.wind_speed_tag_calm, R.color.wind_speed_tag_calm)
            ForecastElement.WindSpeed.Tag.GENTLE -> elementTag(R.string.wind_speed_tag_gentle, R.color.wind_speed_tag_gentle)
            ForecastElement.WindSpeed.Tag.MODERATE -> elementTag(R.string.wind_speed_tag_moderate, R.color.wind_speed_tag_moderate)
            ForecastElement.WindSpeed.Tag.STRONG -> elementTag(R.string.wind_speed_tag_strong, R.color.wind_speed_tag_strong)
            ForecastElement.WindSpeed.Tag.GALE -> elementTag(R.string.wind_speed_tag_gale, R.color.wind_speed_tag_gale)
            ForecastElement.WindSpeed.Tag.DANGEROUS -> elementTag(R.string.wind_speed_tag_dangerous, R.color.wind_speed_tag_dangerous)
        }
    }

    private fun elementTag(stringRes: Int, colorRes: Int) = ElementTag.Tag(stringRes, colorRes)
}