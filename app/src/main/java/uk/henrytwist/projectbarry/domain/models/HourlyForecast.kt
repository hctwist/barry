package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

sealed class HourlyForecast {

    class Conditions(val blocks: List<HourConditionForecast>) : HourlyForecast()

    class Elements(val elements: List<HourElementForecast>) : HourlyForecast()
}

sealed class HourConditionForecast {

    class ConditionBlock(val startTime: ZonedDateTime, val endTime: ZonedDateTime, val condition: WeatherCondition, val isNight: Boolean) : HourConditionForecast() {

        fun sameStartAndEnd() = startTime == endTime
    }

    class NewDay(val time: ZonedDateTime) : HourConditionForecast()
}

sealed class HourElementForecast {

    class Element(val time: ZonedDateTime, val element: ForecastElement<*>, val fraction: Double) : HourElementForecast()

    class NewDay(val time: ZonedDateTime) : HourElementForecast()
}