package uk.henrytwist.projectbarry.domain.util

import java.time.LocalDate
import java.time.ZonedDateTime

object DateTimeUtil {

    fun isToday(time: ZonedDateTime): Boolean {

        return time.toLocalDate().equals(LocalDate.now())
    }
}