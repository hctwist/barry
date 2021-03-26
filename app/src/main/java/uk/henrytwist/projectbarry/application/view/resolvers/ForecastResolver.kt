package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.TimeDisplayUtil
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.ConditionChange
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import uk.henrytwist.projectbarry.domain.models.SunriseSunset
import uk.henrytwist.projectbarry.domain.util.DisplayUtil

object ForecastResolver {

    fun displayTemperature(context: Context, temperature: ScaledTemperature?): String? {

        if (temperature == null) return null

        val formattedValue = DisplayUtil.decimalPlaces(temperature.value, 0)

        return when (temperature.scale) {

            ScaledTemperature.Scale.CELSIUS, ScaledTemperature.Scale.FAHRENHEIT -> context.getString(
                    R.string.format_degrees,
                    formattedValue
            )

            ScaledTemperature.Scale.KELVIN -> context.getString(
                    R.string.format_kelvin,
                    formattedValue
            )
        }
    }

    fun displayFeelsLike(context: Context, temperature: ScaledTemperature?): String? {

        if (temperature == null) return null
        return context.getString(R.string.main_feels_like, displayTemperature(context, temperature))
    }

    fun resolveSunriseSunset(context: Context, sunriseSunset: SunriseSunset?): String? {

        sunriseSunset ?: return null

        val res = if (sunriseSunset.isSunrise) R.string.main_sunrise else R.string.main_sunset
        return context.getString(res, TimeDisplayUtil.displayTime(sunriseSunset.time))
    }

    fun displaySpeed(context: Context, windSpeed: Double): String {

        return context.getString(R.string.format_speed, DisplayUtil.decimalPlaces(windSpeed, 1))
    }

    fun displayPop(context: Context, pop: Double): String {

        return DisplayUtil.percentage(context, pop, 0)
    }

    fun resolveHourlyChange(context: Context, change: ConditionChange?): String {

        if (change == null) return ""

        return when (change) {

            is ConditionChange.Until -> context.getString(
                    R.string.hourly_change_until,
                    WeatherConditionResolver.resolveSimpleName(context, change.current),
                    TimeDisplayUtil.displayMeridiemHour(change.time)
            )

            is ConditionChange.At -> context.getString(
                    R.string.hourly_change_at,
                    WeatherConditionResolver.resolveSimpleName(context, change.future),
                    TimeDisplayUtil.displayMeridiemHour(change.time)
            )

            is ConditionChange.AllDay -> context.getString(
                    R.string.hourly_change_all_day,
                    WeatherConditionResolver.resolveSimpleName(context, change.condition)
            )

            is ConditionChange.Tomorrow -> context.getString(
                    R.string.hourly_change_tomorrow,
                    WeatherConditionResolver.resolveSimpleName(context, change.todayCondition),
                    WeatherConditionResolver.resolveSimpleName(context, change.tomorrowCondition),
                    TimeDisplayUtil.displayMeridiemHour(change.time)
            )
        }
    }

    fun getElementDisplayString(context: Context, element: ForecastElement): String {

        return when (element) {

            is ForecastElement.Temperature -> displayTemperature(context, element.temperature)!!
            is ForecastElement.UVIndex -> DisplayUtil.decimalPlaces(element.index, 1)
            is ForecastElement.Pop -> displayPop(context, element.pop)
            is ForecastElement.FeelsLike -> displayTemperature(context, element.temperature)!!
            is ForecastElement.Humidity -> DisplayUtil.percentage(context, element.humidity)
            is ForecastElement.WindSpeed -> displaySpeed(context, element.speed)
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
}