package com.twisthenry8gmail.projectbarry.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.twisthenry8gmail.projectbarry.Trigger
import com.twisthenry8gmail.projectbarry.core.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.FetchSelectedLocationUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetNowForecastFlowUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetSelectedLocationFlowUseCase
import com.twisthenry8gmail.projectbarry.usecases.NeedsLocationPermissionUseCase
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val getSelectedLocationFlowUseCase: GetSelectedLocationFlowUseCase,
    private val needsLocationPermissionUseCase: NeedsLocationPermissionUseCase,
    private val fetchSelectedLocationUseCase: FetchSelectedLocationUseCase,
    private val getNowForecastFlowUseCase: GetNowForecastFlowUseCase
) : NavigatorViewModel() {

    private val _locationPermissionQuery = MutableLiveData<Trigger>()
    val locationPermissionQuery: LiveData<Trigger>
        get() = _locationPermissionQuery

    private val location = getSelectedLocationFlowUseCase().map { it.successOrNull() }.asLiveData()
    val locationName = location.map { it?.locationData?.name }
    val locationStatus = location.map { it?.status }
    
    private val _showMenu = MutableLiveData<Trigger>()
    val showMenu: LiveData<Trigger>
        get() = _showMenu

    private val currentForecast = getNowForecastFlowUseCase().asLiveData()
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

            if (needsLocationPermissionUseCase()) {

                _locationPermissionQuery.value = Trigger()
            } else {

                fetchSelectedLocation()
//                startCollectingLocation()
            }
            
            currentForecast.observeForever {

                Log.d("MainViewModel", ": ")
            }
        }
    }

    fun fetchSelectedLocation() {

        viewModelScope.launch {

            fetchSelectedLocationUseCase()
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {

        if (granted) {

            fetchSelectedLocation()
//            startCollectingLocation()
        } else {

            TODO()
//            navigateTo(R.id.action_fragmentMain_to_fragmentLocationPermission)
        }
    }

    fun onMenuClick() {

        _showMenu.value = Trigger()
    }
}