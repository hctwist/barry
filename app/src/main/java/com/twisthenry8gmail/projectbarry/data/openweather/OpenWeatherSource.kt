package com.twisthenry8gmail.projectbarry.data.openweather

import com.twisthenry8gmail.projectbarry.data.Result

interface OpenWeatherSource {

    suspend fun getOneCallData(lat: Double, lng: Double): Result<OneCallData>

    class OneCallData(
        val time: Long,
        val lat: Double,
        val lng: Double,
        val sunset: Long,
        val sunrise: Long,
        val temp: Double,
        val conditionCode: Int,
        val feelsLike: Double,
        val hourly: List<Hour>,
        val daily: List<Day>
    ) {

        class Hour(val time: Long, val temp: Double, val conditionCode: Int, val pop: Double)

        class Day(
            val time: Long,
            val tempLow: Double,
            val tempHigh: Double,
            val conditionCode: Int,
            val pop: Double
        )
    }
}