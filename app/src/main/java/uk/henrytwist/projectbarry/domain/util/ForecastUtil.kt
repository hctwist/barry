package uk.henrytwist.projectbarry.domain.util

import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

object ForecastUtil {

    fun isNight(time: ZonedDateTime, forecast: Forecast): Boolean {

        val timeLocalDate = time.toLocalDate()
        forecast.daily.forEach {

            val sunrise = it.sunrise.atZone(ZoneId.systemDefault())

            if (sunrise.toLocalDate() == timeLocalDate) {

                return time.until(sunrise, ChronoUnit.MINUTES) >= 60 || it.sunset.until(time, ChronoUnit.MINUTES) >= 60
            }
        }

        return false
    }
}