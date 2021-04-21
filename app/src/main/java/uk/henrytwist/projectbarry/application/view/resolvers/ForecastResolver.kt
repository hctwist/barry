package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.TimeDisplayUtil
import uk.henrytwist.projectbarry.domain.models.*
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

    fun displaySpeed(context: Context, windSpeed: ScaledSpeed): String {

        return context.getString(when (windSpeed.scale) {

            ScaledSpeed.Scale.METRES_PER_SECOND -> R.string.format_mps
            ScaledSpeed.Scale.MILES_PER_HOUR -> R.string.format_mph
            ScaledSpeed.Scale.KILOMETRES_PER_HOUR -> R.string.format_kph
            ScaledSpeed.Scale.KNOTS -> R.string.format_knots
        }, DisplayUtil.decimalPlaces(windSpeed.value, 1))
    }

    fun displayPop(context: Context, pop: Double): String {

        return DisplayUtil.percentage(context, pop, 0)
    }

    fun displayUVIndex(uvIndex: Double): String {

        return DisplayUtil.decimalPlaces(uvIndex, 1)
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
}