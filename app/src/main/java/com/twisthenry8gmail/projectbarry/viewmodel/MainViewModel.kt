package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.Event
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.view.locations.LocationUtil
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(private val forecastLocationRepository: ForecastLocationRepository) :
    NavigatorViewModel() {

    private val _locationPermissionRequest = MutableLiveData<Event<Unit>>()
    val locationPermissionRequest: LiveData<Event<Unit>>
        get() = _locationPermissionRequest

    private val _location = forecastLocationRepository.selectedLocationFlow
    private val _locationLiveData = MutableLiveData<ForecastLocation>()
    val locationIconRes = _locationLiveData.map { result ->

        result.type.let { LocationUtil.resolveIconRes(it) }
    }
    val locationName = _locationLiveData.map { result ->

        result.name
    }

    private var pendingState = State.NOW
    private val _state = MutableLiveData(State.LOADING_LOCATION)
    val state: LiveData<State>
        get() = _state

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean>
        get() = _refreshing

    init {

        viewModelScope.launch {

            val type = forecastLocationRepository.getSelectedLocationType()

            if (type is Result.Success && type.data == ForecastLocation.Type.LAST_KNOWN_LOCATION || type is Result.Failure) {

                _locationPermissionRequest.value = Event(Unit)
            } else {

                startLoadingLocation()
            }
        }
    }

    private fun startLoadingLocation() {

        viewModelScope.launch {

            delay(2000)

            _location.collect { result ->

                if (result is Result.Success) {

                    _locationLiveData.value = result.data
                    _state.value = pendingState
                } else if (result is Result.Failure) {

                    _state.value = State.LOCATION_ERROR
                }
            }
        }

        viewModelScope.launch {

            forecastLocationRepository.fetchSelectedLocation()
        }
    }

    fun registerPermissionState(state: PermissionState) {

        if (state == PermissionState.GRANTED) {

            startLoadingLocation()
        } else {

            _state.value = State.LOCATION_ERROR
        }
    }

    fun onNavigationItemSelected(id: Int) {

        val newState = when (id) {

            R.id.main_navigation_now -> State.NOW
            R.id.main_navigation_hourly -> State.HOURLY
            R.id.main_navigation_daily -> State.DAILY
            else -> throw RuntimeException("Invalid state")
        }

        if (state.value == State.LOADING_LOCATION) {

            pendingState = newState
        } else {

            _state.value = newState
        }
    }

    // TODO Stop multiple refreshes in short time period
    fun onSwipeRefresh() {

        viewModelScope.launch {

            _refreshing.value = true
        }
    }

    enum class State {

        LOADING_LOCATION, LOCATION_ERROR, NOW, HOURLY, DAILY
    }

    enum class PermissionState {

        WAITING, GRANTED, DENIED
    }
}