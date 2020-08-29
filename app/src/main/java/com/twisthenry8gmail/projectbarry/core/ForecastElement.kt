package com.twisthenry8gmail.projectbarry.core

sealed class ForecastElement {

    open val doubleValue = 0.0

    class Temperature(val temperature: ScaledTemperature) : ForecastElement() {

        override val doubleValue = temperature.value
    }

    class UVIndex(val index: Double) : ForecastElement()

    class Pop(val pop: Double) : ForecastElement() {

        override val doubleValue = pop
    }

    class FeelsLike(val temperature: ScaledTemperature) : ForecastElement()

    class Humidity(val humidity: Int) : ForecastElement()

    class WindSpeed(val speed: Double) : ForecastElement()
}