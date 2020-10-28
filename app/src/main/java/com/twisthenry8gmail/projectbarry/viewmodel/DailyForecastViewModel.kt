package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.DailyForecast
import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.usecases.GetDailyForecastUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetSelectedLocationFlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DailyForecastViewModel @ViewModelInject constructor(
    getSelectedLocationFlowUseCase: GetSelectedLocationFlowUseCase,
    private val getDailyForecastUseCase: GetDailyForecastUseCase
) : ForecastLocationViewModel(getSelectedLocationFlowUseCase) {

    private val _forecast = MutableLiveData<List<DailyForecast>>()
    val forecast: LiveData<List<DailyForecast>>
        get() = _forecast

    init {

        startCollectingLocation()
    }

    override suspend fun onLocationCollected(location: LocationData) {

        fetchForecast(location)
    }

    fun onRetry() {

        fetchForecast()
    }

    private fun fetchForecast() {

        viewModelScope.launch {

            location.value?.locationData?.let {

                onLoading()
                fetchForecast(it)
            }
        }
    }

    private suspend fun fetchForecast(location: LocationData) {

        val forecast = getDailyForecastUseCase(location)

        if (forecast is Result.Success) {

            _forecast.value = forecast.data
            onLoaded()
        } else {

            onForecastError()
        }
    }
}