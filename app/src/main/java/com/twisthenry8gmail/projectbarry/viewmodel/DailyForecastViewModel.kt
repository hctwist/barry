package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.usecases.GetDailyForecastUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
class DailyForecastViewModel @ViewModelInject constructor(
    locationRepository: ForecastLocationRepository,
    private val getDailyForecastUseCase: GetDailyForecastUseCase
) : ForecastLocationViewModel(locationRepository) {

    private val _forecast = MutableLiveData<List<DailyForecast>>()
    val forecast: LiveData<List<DailyForecast>>
        get() = _forecast

    init {

        startCollectingLocation()
    }

    override suspend fun onLocationCollected(location: ForecastLocation) {

        fetchForecast(location)
    }

    private suspend fun fetchForecast(location: ForecastLocation) {

        val forecast = getDailyForecastUseCase(location)

        if (forecast is Result.Success) {

            _forecast.value = forecast.data
            onLoaded()
        } else {

            onForecastError()
        }
    }
}