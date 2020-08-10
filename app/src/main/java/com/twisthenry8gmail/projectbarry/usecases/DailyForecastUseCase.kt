package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.openweather.OpenWeatherRepository
import javax.inject.Inject

class DailyForecastUseCase @Inject constructor(val openWeatherRepository: OpenWeatherRepository) {

    suspend fun getDailyForecasts(location: ForecastLocation, forceRefresh: Boolean) {

        val oneCallData = openWeatherRepository.getOneCallData(location, forceRefresh)

        oneCallData.map {

            it.daily
        }
    }
}