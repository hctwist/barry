package com.twisthenry8gmail.projectbarry.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object TimeUtil {

    fun displayTime(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(dateTime)
    }
}