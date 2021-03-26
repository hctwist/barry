package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

class HourlyForecast<E : ForecastElement>(val hours: List<Hour<E>>, val minElementValue: Double, val maxElementValue: Double, val change: ConditionChange) {

    class Hour<E : ForecastElement>(val time: ZonedDateTime, val condition: WeatherCondition, val element: E)
}