package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.CurrentForecast
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.GetCurrentForecastUseCase
import com.twisthenry8gmail.projectbarry.view.Feature
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CurrentForecastViewModel @ViewModelInject constructor(
    private val forecastLocationRepository: ForecastLocationRepository,
    private val getCurrentForecastUseCase: GetCurrentForecastUseCase
) : ViewModel() {

    private val _state = MutableLiveData(State.LOADING_FORECAST)
    val state: LiveData<State>
        get() = _state

    private val _location = forecastLocationRepository.selectedLocationFlow
    private val _locationLiveData = MutableLiveData<ForecastLocation>()
    val locationName = _locationLiveData.map { result ->

        result.name
    }

    private val currentForecast = MutableLiveData<Result<CurrentForecast>>()
    private val successfulCurrentForecast = currentForecast.map { it.successOrNull() }
    val conditionIcon = successfulCurrentForecast.map {

        it?.let { WeatherConditionDisplay.getImageResource(it.condition, true) }
    }
    val currentTemperature = successfulCurrentForecast.map { it?.temp }
    val lowTemperature = successfulCurrentForecast.map { it?.tempLow }
    val highTemperature = successfulCurrentForecast.map { it?.tempHigh }
    val features = successfulCurrentForecast.map {

        it?.let {
            listOf(
                Feature.UVIndex(it.uvIndex),
                Feature.Pop(it.hourly[0].pop),
                Feature.FeelsLike(it.feelsLike),
                Feature.Humidity(it.humidity),
                Feature.WindSpeed(it.windSpeed)
            )
        } ?: listOf()
    }

    init {

        viewModelScope.launch {

            _location.collect { result ->

                delay(2000)

                result.ifSuccessful {

                    if (_locationLiveData.value?.type == ForecastLocation.Type.PENDING_LOCATION) {

                        _locationLiveData.value = it
                        fetchCurrentForecast(false)
                    } else {

                        _state.value = State.LOADING_FORECAST
                        _locationLiveData.value = it
                        fetchCurrentForecast(false)
                    }
                }
            }
        }
    }

    private suspend fun fetchCurrentForecast(forceRefresh: Boolean) {

        _location.value.successOrNull()?.let { l ->

            val forecast = getCurrentForecastUseCase(l, forceRefresh)

            if (forecast is Result.Success) {

                currentForecast.value = forecast
                _state.value = State.FORECAST_LOADED
            } else {

                _state.value = State.FORECAST_ERROR
            }
        }
    }

    enum class State {

        LOADING_FORECAST, FORECAST_LOADED, FORECAST_ERROR
    }
}