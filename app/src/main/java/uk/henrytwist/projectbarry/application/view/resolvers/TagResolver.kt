package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.components.TagView
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.WeatherCondition
import uk.henrytwist.projectbarry.domain.util.ColorUtil

object TagResolver {

    fun resolveTagBackgroundColor(color: Int): Int {

        return MaterialColors.compositeARGBWithAlpha(color, ColorUtil.ELEMENT_TAG_BACKGROUND_ALPHA)
    }

    fun resolveTag(context: Context, condition: WeatherCondition): TagView.TagContents? {

        return when (condition.group) {

            WeatherCondition.Group.RAIN -> Pair(R.string.condition_rain, R.color.condition_tag_rain)
            WeatherCondition.Group.THUNDER -> Pair(R.string.condition_thunder, R.color.condition_tag_thunder)
            WeatherCondition.Group.SNOW -> Pair(R.string.condition_snow, R.color.condition_tag_snow)
            else -> null
        }?.toTag(context)
    }

    fun resolveTag(context: Context, forecastElement: ForecastElement): TagView.TagContents? {

        return when (forecastElement) {

            is ForecastElement.Temperature -> resolveTag(context, forecastElement)
            is ForecastElement.UVIndex -> resolveTag(context, forecastElement)
            is ForecastElement.Pop -> resolveTag(context, forecastElement)
            is ForecastElement.DewPoint -> resolveTag(context, forecastElement)
            is ForecastElement.WindSpeed -> resolveTag(context, forecastElement)
            else -> null
        }
    }

    private fun resolveTag(context: Context, temperatureElement: ForecastElement.Temperature): TagView.TagContents {

        return when (temperatureElement.getTag()) {

            ForecastElement.Temperature.Tag.FREEZING -> Pair(R.string.temperature_tag_freezing, R.color.temperature_tag_freezing)
            ForecastElement.Temperature.Tag.CHILLY -> Pair(R.string.temperature_tag_chilly, R.color.temperature_tag_chilly)
            ForecastElement.Temperature.Tag.MODERATE -> Pair(R.string.temperature_tag_moderate, R.color.temperature_tag_moderate)
            ForecastElement.Temperature.Tag.WARM -> Pair(R.string.temperature_tag_warm, R.color.temperature_tag_warm)
            ForecastElement.Temperature.Tag.HOT -> Pair(R.string.temperature_tag_hot, R.color.temperature_tag_hot)
        }.toTag(context)
    }

    private fun resolveTag(context: Context, uvElement: ForecastElement.UVIndex): TagView.TagContents {

        return when (uvElement.getTag()) {

            ForecastElement.UVIndex.Tag.ZERO -> Pair(R.string.uv_tag_zero, R.color.uv_tag_zero)
            ForecastElement.UVIndex.Tag.LOW -> Pair(R.string.uv_tag_low, R.color.uv_tag_low)
            ForecastElement.UVIndex.Tag.MODERATE -> Pair(R.string.uv_tag_moderate, R.color.uv_tag_moderate)
            ForecastElement.UVIndex.Tag.HIGH -> Pair(R.string.uv_tag_high, R.color.uv_tag_high)
            ForecastElement.UVIndex.Tag.VERY_HIGH -> Pair(R.string.uv_tag_very_high, R.color.uv_tag_very_high)
            ForecastElement.UVIndex.Tag.EXTREMELY_HIGH -> Pair(R.string.uv_tag_extremely_high, R.color.uv_tag_extremely_high)
        }.toTag(context)
    }

    private fun resolveTag(context: Context, popElement: ForecastElement.Pop): TagView.TagContents {

        return when (popElement.getTag()) {

            ForecastElement.Pop.Tag.NONE -> Pair(R.string.pop_tag_none, R.color.pop_tag_none)
            ForecastElement.Pop.Tag.UNLIKELY -> Pair(R.string.pop_tag_unlikely, R.color.pop_tag_unlikely)
            ForecastElement.Pop.Tag.POSSIBLE -> Pair(R.string.pop_tag_possible, R.color.pop_tag_possible)
            ForecastElement.Pop.Tag.LIKELY -> Pair(R.string.pop_tag_likely, R.color.pop_tag_likely)
            ForecastElement.Pop.Tag.VERY_LIKELY -> Pair(R.string.pop_tag_very_likely, R.color.pop_tag_very_likely)
        }.toTag(context)
    }

    private fun resolveTag(context: Context, humidityElement: ForecastElement.DewPoint): TagView.TagContents {

        return when (humidityElement.getTag()) {

            ForecastElement.DewPoint.Tag.COMFORTABLE -> Pair(R.string.dew_point_comfortable, R.color.dew_point_tag_comfortable)
            ForecastElement.DewPoint.Tag.MUGGY -> Pair(R.string.dew_point_muggy, R.color.dew_point_tag_muggy)
            ForecastElement.DewPoint.Tag.UNCOMFORTABLE -> Pair(R.string.dew_point_uncomfortable, R.color.dew_point_tag_uncomfortable)
        }.toTag(context)
    }

    private fun resolveTag(context: Context, windSpeedElement: ForecastElement.WindSpeed): TagView.TagContents {

        return when (windSpeedElement.getTag()) {

            ForecastElement.WindSpeed.Tag.CALM -> Pair(R.string.wind_speed_tag_calm, R.color.wind_speed_tag_calm)
            ForecastElement.WindSpeed.Tag.GENTLE -> Pair(R.string.wind_speed_tag_gentle, R.color.wind_speed_tag_gentle)
            ForecastElement.WindSpeed.Tag.MODERATE -> Pair(R.string.wind_speed_tag_moderate, R.color.wind_speed_tag_moderate)
            ForecastElement.WindSpeed.Tag.STRONG -> Pair(R.string.wind_speed_tag_strong, R.color.wind_speed_tag_strong)
            ForecastElement.WindSpeed.Tag.GALE -> Pair(R.string.wind_speed_tag_gale, R.color.wind_speed_tag_gale)
            ForecastElement.WindSpeed.Tag.DANGEROUS -> Pair(R.string.wind_speed_tag_dangerous, R.color.wind_speed_tag_dangerous)
        }.toTag(context)
    }

    private fun Pair<Int, Int>.toTag(context: Context) = TagView.TagContents(context.getString(first), ContextCompat.getColor(context, second))
}