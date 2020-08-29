package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.core.MainState
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.ForecastElement
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.usecases.GetHourlyPopForecastUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetHourlyTemperatureForecastUseCase
import com.twisthenry8gmail.projectbarry.view.hourly.HourlyForecastView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HourlyForecastViewModel @ViewModelInject constructor(
    private val forecastLocationRepository: ForecastLocationRepository,
    private val getHourlyTemperatureForecastUseCase: GetHourlyTemperatureForecastUseCase,
    private val getHourlyPopForecastUseCase: GetHourlyPopForecastUseCase
) : ViewModel() {

    private val _state = MutableLiveData(MainState.LOADING)
    val state: LiveData<MainState>
        get() = _state

    private val _location = forecastLocationRepository.selectedLocationFlow
    private val _successfulLocation = MutableLiveData<ForecastLocation>()
    val successfulLocation: LiveData<ForecastLocation>
        get() = _successfulLocation

    var forecastType = ForecastType.TEMPERATURE
        private set

    private val _hourlyForecast = MutableLiveData<List<HourlyForecastView.Hour>>()
    val hourlyForecast: LiveData<List<HourlyForecastView.Hour>>
        get() = _hourlyForecast

    private val _dataLoading = MutableLiveData(ForecastState.LOADING)
    val dataLoading: LiveData<ForecastState>
        get() = _dataLoading

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

                        loadData()
                    }

                    is Result.Failure -> {

                        _state.value = MainState.LOCATION_ERROR
                    }
                }
            }
        }
    }

    fun onForecastTypeSelected(forecastType: ForecastType) {

        this.forecastType = forecastType
        viewModelScope.launch {

            loadData()
        }
    }

    private suspend fun loadData() {

        _dataLoading.value = ForecastState.LOADING
        _location.value.ifSuccessful { loc ->

            when (forecastType) {

                ForecastType.TEMPERATURE -> {

                    val tempForecast = getHourlyTemperatureForecastUseCase(loc)
                    if (tempForecast is Result.Success) {

                        _hourlyForecast.value = tempForecast.data.map {

                            HourlyForecastView.Hour(

                                it.time,
                                it.condition,
                                ForecastElement.Temperature(it.temperature)
                            )
                        }
                        _dataLoading.value = ForecastState.LOADED
                    } else {

                        _dataLoading.value = ForecastState.ERROR
                    }
                }

                ForecastType.POP -> {

                    val popForecast = getHourlyPopForecastUseCase(loc)
                    if (popForecast is Result.Success) {

                        _hourlyForecast.value = popForecast.data.map {

                            HourlyForecastView.Hour(

                                it.time,
                                it.condition,
                                ForecastElement.Pop(it.pop)
                            )
                        }
                        _dataLoading.value = ForecastState.LOADED
                    } else {

                        _dataLoading.value = ForecastState.ERROR
                    }
                }

                ForecastType.UV -> TODO()
            }
        }
    }

    enum class ForecastState {

        LOADING, LOADED, ERROR
    }

    enum class ForecastType {

        TEMPERATURE, POP, UV
    }
}