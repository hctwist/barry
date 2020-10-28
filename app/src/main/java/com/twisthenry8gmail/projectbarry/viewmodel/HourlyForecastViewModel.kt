package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.HourlyForecast2
import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.usecases.GetHourlyPopForecastUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetHourlyTemperatureForecastUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetSelectedLocationFlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HourlyForecastViewModel @ViewModelInject constructor(
    getSelectedLocationFlowUseCase: GetSelectedLocationFlowUseCase,
    private val getHourlyTemperatureForecastUseCase: GetHourlyTemperatureForecastUseCase,
    private val getHourlyPopForecastUseCase: GetHourlyPopForecastUseCase
) : ForecastLocationViewModel(getSelectedLocationFlowUseCase) {

    private val _forecastType = MutableLiveData(ForecastType.TEMPERATURE)
    val forecastType: LiveData<ForecastType>
        get() = _forecastType

    private val _hourlyForecast = MutableLiveData<HourlyForecast2>()
    val hourlyForecast: LiveData<HourlyForecast2>
        get() = _hourlyForecast

    private val _dataLoading = MutableLiveData(ForecastState.LOADING)
    val dataLoading: LiveData<ForecastState>
        get() = _dataLoading

    init {

        startCollectingLocation()
    }

    override suspend fun onLocationCollected(location: LocationData) {

        loadForecast(location)
    }

    fun onRetry() {

        viewModelScope.launch {

            location.value?.locationData?.let {

                loadForecast(it)
            }
        }
    }

    fun onForecastTypeSelected(forecastType: ForecastType) {

        _forecastType.value = forecastType
        location.value?.locationData?.let {

            viewModelScope.launch {

                loadForecast(it)
            }
        }
    }

    private suspend fun loadForecast(location: LocationData) {

        _dataLoading.value = ForecastState.LOADING

        val result = when (forecastType.value!!) {

            ForecastType.TEMPERATURE -> getHourlyTemperatureForecastUseCase(location)
            ForecastType.POP -> getHourlyPopForecastUseCase(location)
            ForecastType.UV -> TODO()
        }

        if (result is Result.Success) {

            _hourlyForecast.value = result.data
            _dataLoading.value = ForecastState.LOADED
        } else {

            _dataLoading.value = ForecastState.ERROR
        }
    }

    enum class ForecastState {

        LOADING, LOADED, ERROR
    }

    enum class ForecastType(val titleRes: Int) {

        TEMPERATURE(R.string.element_temperature), POP(R.string.element_pop), UV(R.string.element_uv_index)
    }
}