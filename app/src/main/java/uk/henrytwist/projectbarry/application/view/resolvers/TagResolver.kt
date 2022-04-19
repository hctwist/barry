package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import androidx.core.content.ContextCompat
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.components.TagView
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.WeatherCondition

object TagResolver {

    fun resolveTag(context: Context, condition: WeatherCondition): TagView.TagContents? {

        return when (condition.group) {

            WeatherCondition.Group.RAIN -> Pair(R.string.condition_rain, R.color.condition_tag_rain)
            WeatherCondition.Group.THUNDER -> Pair(R.string.condition_thunder, R.color.condition_tag_thunder)
            WeatherCondition.Group.SNOW -> Pair(R.string.condition_snow, R.color.condition_tag_snow)
            else -> null
        }?.toTag(context)
    }

    fun resolveTag(context: Context, forecastElement: ForecastElement): TagView.TagContents? {

        return when (forecastElement.getTag()) {

            ForecastElement.Tag.TEMP_FREEZING -> Pair(R.string.temperature_tag_freezing, R.color.temperature_tag_freezing)
            ForecastElement.Tag.TEMP_CHILLY -> Pair(R.string.temperature_tag_chilly, R.color.temperature_tag_chilly)
            ForecastElement.Tag.TEMP_MODERATE -> Pair(R.string.temperature_tag_moderate, R.color.temperature_tag_moderate)
            ForecastElement.Tag.TEMP_WARM -> Pair(R.string.temperature_tag_warm, R.color.temperature_tag_warm)
            ForecastElement.Tag.TEMP_HOT -> Pair(R.string.temperature_tag_hot, R.color.temperature_tag_hot)
            ForecastElement.Tag.UV_ZERO -> Pair(R.string.uv_tag_zero, R.color.uv_tag_zero)
            ForecastElement.Tag.UV_LOW -> Pair(R.string.uv_tag_low, R.color.uv_tag_low)
            ForecastElement.Tag.UV_MODERATE -> Pair(R.string.uv_tag_moderate, R.color.uv_tag_moderate)
            ForecastElement.Tag.UV_HIGH -> Pair(R.string.uv_tag_high, R.color.uv_tag_high)
            ForecastElement.Tag.UV_VERY_HIGH -> Pair(R.string.uv_tag_very_high, R.color.uv_tag_very_high)
            ForecastElement.Tag.UV_EXTREMELY_HIGH -> Pair(R.string.uv_tag_extremely_high, R.color.uv_tag_extremely_high)
            ForecastElement.Tag.POP_NONE -> Pair(R.string.pop_tag_none, R.color.pop_tag_none)
            ForecastElement.Tag.POP_UNLIKELY -> Pair(R.string.pop_tag_unlikely, R.color.pop_tag_unlikely)
            ForecastElement.Tag.POP_POSSIBLE -> Pair(R.string.pop_tag_possible, R.color.pop_tag_possible)
            ForecastElement.Tag.POP_LIKELY -> Pair(R.string.pop_tag_likely, R.color.pop_tag_likely)
            ForecastElement.Tag.POP_VERY_LIKELY -> Pair(R.string.pop_tag_very_likely, R.color.pop_tag_very_likely)
            ForecastElement.Tag.DEW_POINT_COMFORTABLE -> Pair(R.string.dew_point_comfortable, R.color.dew_point_tag_comfortable)
            ForecastElement.Tag.DEW_POINT_MUGGY -> Pair(R.string.dew_point_muggy, R.color.dew_point_tag_muggy)
            ForecastElement.Tag.DEW_POINT_UNCOMFORTABLE -> Pair(R.string.dew_point_uncomfortable, R.color.dew_point_tag_uncomfortable)
            ForecastElement.Tag.SPEED_CALM -> Pair(R.string.wind_speed_tag_calm, R.color.wind_speed_tag_calm)
            ForecastElement.Tag.SPEED_GENTLE -> Pair(R.string.wind_speed_tag_gentle, R.color.wind_speed_tag_gentle)
            ForecastElement.Tag.SPEED_MODERATE -> Pair(R.string.wind_speed_tag_moderate, R.color.wind_speed_tag_moderate)
            ForecastElement.Tag.SPEED_STRONG -> Pair(R.string.wind_speed_tag_strong, R.color.wind_speed_tag_strong)
            ForecastElement.Tag.SPEED_GALE -> Pair(R.string.wind_speed_tag_gale, R.color.wind_speed_tag_gale)
            ForecastElement.Tag.SPEED_DANGEROUS -> Pair(R.string.wind_speed_tag_dangerous, R.color.wind_speed_tag_dangerous)
        }.toTag(context)
    }

    private fun Pair<Int, Int>.toTag(context: Context) = TagView.TagContents(context.getString(first), ContextCompat.getColor(context, second))
}