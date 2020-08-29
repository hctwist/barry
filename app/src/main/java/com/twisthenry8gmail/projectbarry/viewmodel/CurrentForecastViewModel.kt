package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.core.ForecastElement
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.successOrNull
import com.twisthenry8gmail.projectbarry.data.CurrentForecast
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.usecases.GetNowForecastUseCase
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CurrentForecastViewModel @ViewModelInject constructor(
    locationRepository: ForecastLocationRepository,
    private val getNowForecastUseCase: GetNowForecastUseCase
) : ForecastLocationViewModel(locationRepository) {

    private val currentForecast = MutableLiveData<Result<CurrentForecast>>()
    private val successfulCurrentForecast = currentForecast.map { it.successOrNull() }
    val condition = successfulCurrentForecast.map {

        it?.let { WeatherConditionDisplay.getStringResource(it.condition) }
    }
    val conditionIcon = successfulCurrentForecast.map {

        it?.let { WeatherConditionDisplay.getImageResource(it.condition, true) }
    }
    val currentTemperature = successfulCurrentForecast.map { it?.temp }
    val lowTemperature = successfulCurrentForecast.map { it?.tempLow }
    val highTemperature = successfulCurrentForecast.map { it?.tempHigh }
    val elements = successfulCurrentForecast.map {

        it?.let {
            listOf(
                ForecastElement.UVIndex(it.uvIndex),
                ForecastElement.Pop(it.pop),
                ForecastElement.FeelsLike(it.feelsLike),
                ForecastElement.Humidity(it.humidity),
                ForecastElement.WindSpeed(it.windSpeed)
            )
        } ?: listOf()
    }

    init {

        startCollectingLocation()
    }

    override suspend fun onLocationCollected(location: ForecastLocation) {

        fetchForecast(location)
    }

    fun onSwipeRefresh() {

        viewModelScope.launch {

            location.value?.let {

                onLoading()
                fetchForecast(it)
            }
        }
    }

    private suspend fun fetchForecast(location: ForecastLocation) {

        val forecast = getNowForecastUseCase(location)

        if (forecast is Result.Success) {

            currentForecast.value = forecast
            onLoaded()
        } else {

            onForecastError()
        }
    }
}