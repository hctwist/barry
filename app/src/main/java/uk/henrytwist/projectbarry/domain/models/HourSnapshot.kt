package uk.henrytwist.projectbarry.domain.models

import java.time.ZonedDateTime

class HourSnapshot(val time: ZonedDateTime, val condition: WeatherCondition, val important: Boolean)