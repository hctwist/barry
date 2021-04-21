package uk.henrytwist.projectbarry.application.view.hourly

import uk.henrytwist.projectbarry.R

enum class HourlyForecastType(val nameRes: Int) {

    FORECAST(R.string.hourly_forecast_title),
    TEMPERATURE(R.string.element_temperature),
    POP(R.string.element_pop),
    UV(R.string.element_uv_index),
    WIND_SPEED(R.string.element_wind_speed)
}