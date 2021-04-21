package uk.henrytwist.projectbarry.domain.util

import android.content.Context
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*

object DateTimeUtil {

    fun isToday(time: ZonedDateTime): Boolean {

        return time.toLocalDate().equals(LocalDate.now())
    }
}