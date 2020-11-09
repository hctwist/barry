package com.twisthenry8gmail.projectbarry.view

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object TimeDisplayUtil {

    fun displayTime(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(dateTime)
    }

    fun displayHumanDay(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofPattern("EEEE").format(dateTime)
    }

    fun displayMeridiemHour(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofPattern("ha").format(dateTime)
    }

    fun displayDayOfMonth(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofPattern("d").format(dateTime)
    }
}