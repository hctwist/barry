package com.twisthenry8gmail.projectbarry.domain.core

import java.time.ZonedDateTime

sealed class ForecastElement {

    class Temperature(val temperature: ScaledTemperature) : ForecastElement()

    class UVIndex(val index: Double) : ForecastElement()

    class Pop(val pop: Double) : ForecastElement()

    class FeelsLike(val temperature: ScaledTemperature) : ForecastElement()

    class Humidity(val humidity: Int) : ForecastElement()

    class WindSpeed(val speed: Double) : ForecastElement()

    class Sunset(val time: ZonedDateTime): ForecastElement()

    class Sunrise(val time: ZonedDateTime): ForecastElement()
}