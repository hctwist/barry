package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.CurrentForecast
import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.FetchSelectedLocationUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetNowForecastUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetSelectedLocationFlowUseCase
import com.twisthenry8gmail.projectbarry.usecases.NeedsLocationPermissionUseCase
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ForecastViewModel @ViewModelInject constructor(
    private val needsLocationPermissionUseCase: NeedsLocationPermissionUseCase,
    private val fetchSelectedLocationUseCase: FetchSelectedLocationUseCase,
    getSelectedLocationFlowUseCase: GetSelectedLocationFlowUseCase,
    private val getNowForecastUseCase: GetNowForecastUseCase
) : ForecastLocationViewModel(getSelectedLocationFlowUseCase) {

    private val currentForecast = MutableLiveData<Result<CurrentForecast>>()
    private val successfulCurrentForecast = currentForecast.map { it.successOrNull() }
    val condition = successfulCurrentForecast.map {

        it?.let { WeatherConditionDisplay.getStringResource(it.condition) }
    }
    val conditionIcon = successfulCurrentForecast.map {

        it?.let { WeatherConditionDisplay.getImageResource(it.condition) }
    }
    val currentTemperature = successfulCurrentForecast.map { it?.temp }
    val lowTemperature = successfulCurrentForecast.map { it?.tempLow }
    val highTemperature = successfulCurrentForecast.map { it?.tempHigh }
    val elements = successfulCurrentForecast.map { it?.elements ?: listOf() }

    val hourSnapshots = successfulCurrentForecast.map { it?.hourSnapshots ?: listOf() }

    init {

        viewModelScope.launch {

            startCollectingLocation()
        }
    }

    override suspend fun onLocationCollected(location: LocationData) {

        fetchForecast(location)
    }

    fun onLocationPermissionResult(granted: Boolean) {

        if (granted) {

            viewModelScope.launch {

                fetchSelectedLocationUseCase()
                startCollectingLocation()
            }
        } else {

            TODO()
//            navigateTo(R.id.action_fragmentMain_to_fragmentLocationPermission)
        }
    }

    private suspend fun fetchForecast(location: LocationData) {

        val forecast = getNowForecastUseCase(location)

        if (forecast is Result.Success) {

            currentForecast.value = forecast
            onLoaded()
        } else {

            onForecastError()
        }
    }
}