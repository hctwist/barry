package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.Trigger
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(private val forecastLocationRepository: ForecastLocationRepository) :
    NavigatorViewModel() {

    private val _locationPermissionQuery = MutableLiveData<Trigger>()
    val locationPermissionQuery: LiveData<Trigger>
        get() = _locationPermissionQuery

    private val _state = MutableLiveData(State.WAITING)
    val state: LiveData<State>
        get() = _state

    init {

        viewModelScope.launch {

            val selectedLocationType = forecastLocationRepository.getSelectedLocationType()

            if (selectedLocationType is Result.Success && selectedLocationType.data == ForecastLocation.Type.LAST_KNOWN_LOCATION || selectedLocationType is Result.Failure) {

                _locationPermissionQuery.value = Trigger()
            } else {

                startLoadingLocation()
            }
        }
    }

    private fun startLoadingLocation() {

        viewModelScope.launch {

            forecastLocationRepository.fetchSelectedLocation()
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {

        if (_state.value == State.WAITING && granted) {

            _state.value = State.NOW
            startLoadingLocation()
        } else {

            navigateTo(R.id.action_fragmentMain_to_fragmentLocationPermission)
        }
    }

    fun onNavigationItemSelected(id: Int) {

        _state.value = when (id) {

            R.id.main_navigation_now -> State.NOW
            R.id.main_navigation_hourly -> State.HOURLY
            R.id.main_navigation_daily -> State.DAILY
            else -> throw RuntimeException("Invalid state")
        }
    }

    fun onMenuClick() {

        // TODO
    }

    enum class State {

        WAITING, NOW, HOURLY, DAILY
    }
}