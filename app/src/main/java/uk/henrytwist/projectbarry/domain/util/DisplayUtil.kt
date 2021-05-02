package uk.henrytwist.projectbarry.domain.util

import android.content.Context
import uk.henrytwist.projectbarry.R
import java.math.RoundingMode
import java.text.DecimalFormat

object DisplayUtil {

    fun decimalPlaces(double: Double, number: Int): String {

        val patternBuilder = StringBuilder(number + 2)

        patternBuilder.append("#")
        if (number > 0) patternBuilder.append(".")
        for (i in 0 until number) patternBuilder.append("#")

        val decimalFormat = DecimalFormat(patternBuilder.toString())
        decimalFormat.roundingMode = RoundingMode.HALF_UP

        // FIXME Better method needed
        val formatted = decimalFormat.format(double)
        return if (formatted == "-0") "0" else formatted
    }

    fun percentage(context: Context, double: Double, decimalPlaces: Int): String {

        return context.getString(
                R.string.format_percentage,
                decimalPlaces(double * 100, decimalPlaces)
        )
    }
}