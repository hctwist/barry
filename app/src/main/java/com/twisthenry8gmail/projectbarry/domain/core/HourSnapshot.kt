package com.twisthenry8gmail.projectbarry.domain.core

import java.time.ZonedDateTime

class HourSnapshot(val time: ZonedDateTime, val condition: WeatherCondition, val important: Boolean)