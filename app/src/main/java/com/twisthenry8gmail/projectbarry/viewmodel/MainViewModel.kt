package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.Trigger
import com.twisthenry8gmail.projectbarry.core.successOrNull
import com.twisthenry8gmail.projectbarry.usecases.FetchSelectedLocationUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetNowForecastFlowUseCase
import com.twisthenry8gmail.projectbarry.usecases.GetSelectedLocationFlowUseCase
import com.twisthenry8gmail.projectbarry.usecases.NeedsLocationPermissionUseCase
import com.twisthenry8gmail.projectbarry.view.WeatherConditionResolver
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

        it?.let { WeatherConditionResolver.resolveStringResource(it.condition) }
    }
    val conditionArt = successfulCurrentForecast.map {

        it?.let { WeatherConditionResolver.resolveArtResource(it.condition) }
    }
    val conditionArtColor = successfulCurrentForecast.map {

        it?.let { WeatherConditionResolver.resolveArtColor(it.condition) }
    }
    val currentTemperature = successfulCurrentForecast.map { it?.temp }
    val elements = successfulCurrentForecast.map { it?.elements ?: listOf() }

    val hourSnapshots = successfulCurrentForecast.map { it?.hourSnapshots ?: listOf() }

    val daySnapshots = successfulCurrentForecast.map { it?.daySnapshots ?: listOf() }

    init {

        viewModelScope.launch {

            if (needsLocationPermissionUseCase()) {

                _locationPermissionQuery.value = Trigger()
            } else {

                fetchSelectedLocation()
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
        } else {

            navigateTo(R.id.action_fragmentMain2_to_fragmentLocationPermission)
        }
    }

    fun onMenuClick() {

        _showMenu.value = Trigger()
    }
}