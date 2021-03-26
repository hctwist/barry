package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

sealed class ConditionChange {

    class Until(val time: ZonedDateTime, val current: WeatherCondition, val tomorrow: Boolean) : ConditionChange()

    class At(val time: ZonedDateTime, val future: WeatherCondition) : ConditionChange()

    class AllDay(val condition: WeatherCondition) : ConditionChange()

    class Tomorrow(val todayCondition: WeatherCondition, val tomorrowCondition: WeatherCondition, val time: ZonedDateTime) : ConditionChange()
}