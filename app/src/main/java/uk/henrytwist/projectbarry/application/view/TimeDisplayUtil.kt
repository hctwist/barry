package uk.henrytwist.projectbarry.application.view

import android.content.Context
import uk.henrytwist.projectbarry.R
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

object TimeDisplayUtil {

    fun displayTime(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(dateTime)
    }

    fun displayDay(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofPattern("EEEE").format(dateTime)
    }

    fun displayMeridiemHour(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofPattern("ha").format(dateTime)
    }

    fun displayDayOfMonth(dateTime: ZonedDateTime): String {

        return DateTimeFormatter.ofPattern("d").format(dateTime)
    }

    fun displayDayOrToday(context: Context, time: ZonedDateTime): String {

        return if (time.toLocalDate() == LocalDate.now()) {

            context.getString(R.string.today)
        } else {

            displayDay(time)
        }
    }

    fun displayDayOrTomorrow(context: Context, time: ZonedDateTime): String {

        val now = LocalDate.now()

        return when (now.until(time.toLocalDate(), ChronoUnit.DAYS).toInt()) {

            1 -> context.getString(R.string.tomorrow)
            else -> displayDay(time)
        }
    }
}