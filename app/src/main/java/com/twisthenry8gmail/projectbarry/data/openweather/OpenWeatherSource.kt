package com.twisthenry8gmail.projectbarry.data.openweather

import com.twisthenry8gmail.projectbarry.core.Result

interface OpenWeatherSource {

    suspend fun getOneCallData(lat: Double, lng: Double): Result<OneCallData>

    class OneCallData(
        val time: Long,
        val lat: Double,
        val lng: Double,
        val temp: Double,
        val conditionCode: Int,
        val feelsLike: Double,
        val humidity: Int,
        val windSpeed: Double,
        val hourly: List<Hour>,
        val daily: List<Day>
    ) {

        class Hour(val time: Long, val temp: Double, val conditionCode: Int, val pop: Double)

        class Day(
            val time: Long,
            val tempLow: Double,
            val tempHigh: Double,
            val conditionCode: Int,
            val pop: Double,
            val sunrise: Long,
            val sunset: Long
        )
    }
}