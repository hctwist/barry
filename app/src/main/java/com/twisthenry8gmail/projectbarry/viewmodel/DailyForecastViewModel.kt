package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.GetDailyForecastUseCase
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DailyForecastViewModel @ViewModelInject constructor(
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
    private val locationRepository: ForecastLocationRepository
) : NavigatorViewModel() {

    private val _state = MutableLiveData(State.LOADING)
    val state: LiveData<State>
        get() = _state

    private val _location = locationRepository.selectedLocationFlow
    val successfulLocation = _location.asLiveData().map { it.successOrNull() }.distinctUntilChanged()

    private val _forecast = MutableLiveData<List<DailyForecast>>()
    val forecast: LiveData<List<DailyForecast>>
        get() = _forecast

    init {

        viewModelScope.launch {

            _location.collect { result ->

                result.ifSuccessful {

                    fetchForecast(it)
                }
            }
        }
    }

    private suspend fun fetchForecast(location: ForecastLocation) {

        val forecast = getDailyForecastUseCase(location)

        if (forecast is Result.Success) {

            _forecast.value = forecast.data
            _state.value = State.LOADED
        } else {

            _state.value = State.ERROR
        }
    }

    enum class State {

        LOADING, LOADED, ERROR
    }
}