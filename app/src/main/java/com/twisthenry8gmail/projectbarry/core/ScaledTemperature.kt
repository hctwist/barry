package com.twisthenry8gmail.projectbarry.core

class ScaledTemperature private constructor(val value: Double, val scale: Scale) {

    fun celsius() = when (scale) {

        Scale.CELSIUS -> value
        Scale.FAHRENHEIT -> fahrenheitToCelsius(value)
        Scale.KELVIN -> kelvinToCelsius(value)
    }

    fun fahrenheit() = when (scale) {

        Scale.CELSIUS -> celsiusToFahrenheit(value)
        Scale.FAHRENHEIT -> value
        Scale.KELVIN -> celsiusToKelvin(kelvinToCelsius(value))
    }

    fun kelvin() = when (scale) {

        Scale.CELSIUS -> celsiusToKelvin(value)
        Scale.FAHRENHEIT -> celsiusToKelvin(fahrenheitToCelsius(value))
        Scale.KELVIN -> value
    }

    fun to(scale: Scale) = ScaledTemperature(
        when (scale) {

            Scale.CELSIUS -> celsius()
            Scale.FAHRENHEIT -> fahrenheit()
            Scale.KELVIN -> kelvin()
        }, scale
    )

    companion object {

        fun fromKelvin(value: Double) = ScaledTemperature(value, Scale.KELVIN)

        private fun fahrenheitToCelsius(fahrenheit: Double) = (fahrenheit - 32) * (5.0 / 9)
        private fun celsiusToFahrenheit(celsius: Double) = celsius * (9.0 / 5) + 32
        private fun celsiusToKelvin(celsius: Double) = celsius + 273.15
        private fun kelvinToCelsius(kelvin: Double) = kelvin - 273.15
    }

    enum class Scale {

        CELSIUS, FAHRENHEIT, KELVIN
    }
}