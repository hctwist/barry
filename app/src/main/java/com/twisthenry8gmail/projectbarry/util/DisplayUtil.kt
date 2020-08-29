package com.twisthenry8gmail.projectbarry.util

import android.content.Context
import com.twisthenry8gmail.projectbarry.R
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

        return decimalFormat.format(double)
    }

    fun percentage(context: Context, double: Double, decimalPlaces: Int): String {

        return context.getString(
            R.string.percentage,
            decimalPlaces(double * 100, decimalPlaces)
        )
    }

    fun percentage(context: Context, i: Int): String {

        return context.getString(R.string.percentage, i.toString())
    }
}