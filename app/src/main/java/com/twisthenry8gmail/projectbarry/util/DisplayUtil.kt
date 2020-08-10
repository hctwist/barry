package com.twisthenry8gmail.projectbarry.util

import android.content.Context
import com.twisthenry8gmail.projectbarry.R
import java.text.DecimalFormat

object DisplayUtil {

    fun decimalPlaces(double: Double, number: Int): String {

        val patternBuilder = StringBuilder(number + 2)

        patternBuilder.append("#")
        if (number > 0) patternBuilder.append(".")
        for (i in 0 until number) patternBuilder.append("#")

        return DecimalFormat(patternBuilder.toString()).format(double)
    }

    fun percentage(context: Context, double: Double, decimalPlaces: Int): String {

        return context.getString(
            R.string.percentage,
            decimalPlaces(double * 100, decimalPlaces)
        )
    }
}