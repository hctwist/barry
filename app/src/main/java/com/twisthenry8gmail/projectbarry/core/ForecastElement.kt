package com.twisthenry8gmail.projectbarry.core

sealed class ForecastElement {

    class Temperature(val temperature: ScaledTemperature): ForecastElement()
    class UVIndex(val index: Double): ForecastElement()
    class Pop(val pop: Double): ForecastElement()
    class FeelsLike(val temperature: ScaledTemperature): ForecastElement()
    class Humidity(val humidity: Int): ForecastElement()
    class WindSpeed(val speed: Double): ForecastElement()
}