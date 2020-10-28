package com.twisthenry8gmail.projectbarry.data

import kotlin.math.pow
import kotlin.math.sqrt

object DataUtil {

    private const val LAT_LNG_CLOSE_THRESHOLD = 0.001

    fun latLngClose(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Boolean {

        return sqrt((lat1 - lat2).pow(2) + (lng1 - lng2).pow(2)) < LAT_LNG_CLOSE_THRESHOLD
    }
}