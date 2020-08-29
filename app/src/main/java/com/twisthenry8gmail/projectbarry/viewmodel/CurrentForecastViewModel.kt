package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.twisthenry8gmail.projectbarry.MainState
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.core.ForecastElement
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.CurrentForecast
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.GetNowForecastUseCase
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CurrentForecastViewModel @ViewModelInject constructor(
    private val forecastLocationRepository: ForecastLocationRepository,
    private val getNowForecastUseCase: GetNowForecastUseCase
) : ViewModel() {

    private val _state = MutableLiveData(MainState.LOADING)
    val state: LiveData<MainState>
        get() = _state

    private val _location = forecastLocationRepository.selectedLocationFlow
    private val _successfulLocation = MutableLiveData<ForecastLocation>()
    val successfulLocation: LiveData<ForecastLocation>
        get() = _successfulLocation

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

    private var fetchForecastJob: Job? = null

    init {

        viewModelScope.launch {

            _location.collect {

                when (it) {

                    is Result.Success -> {

                        _successfulLocation.value = it.data
                        if (it.data.type !in arrayOf(
                                ForecastLocation.Type.LAST_KNOWN_LOCATION,
                                ForecastLocation.Type.CURRENT_LOCATION
                            )
                        ) {

                            _state.value = MainState.LOADING
                        }

                        fetchForecastJob?.cancel()
                        fetchForecastJob = viewModelScope.launch {

                            fetchForecast(it.data)
                        }
                    }

                    is Result.Failure -> {

                        _state.value = MainState.LOCATION_ERROR
                    }
                }
            }
        }
    }

    fun onSwipeRefresh() {

        viewModelScope.launch {

            _location.value.ifSuccessful {

                _state.value = MainState.LOADING
                fetchForecast(it)
            }
        }
    }

    private suspend fun fetchForecast(location: ForecastLocation) {

        val forecast = getNowForecastUseCase(location)

        if (forecast is Result.Success) {

            currentForecast.value = forecast
            _state.value = MainState.LOADED
        } else {

            _state.value = MainState.FORECAST_ERROR
        }
    }
}