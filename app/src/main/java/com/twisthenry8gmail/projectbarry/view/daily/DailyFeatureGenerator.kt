package com.twisthenry8gmail.projectbarry.view.daily

import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.view.Feature

object DailyFeatureGenerator {

    fun temperatureLow(forecast: DailyForecast) = Feature.TemperatureLow(forecast.tempLow)

    fun temperatureHigh(forecast: DailyForecast) = Feature.TemperatureHigh(forecast.tempHigh)

    fun pop(forecast: DailyForecast) = Feature.Pop(forecast.pop)
}