package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.MainState
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class ForecastLocationViewModel(locationRepository: ForecastLocationRepository) :
    NavigatorViewModel() {

    private val _state = MutableLiveData(MainState.LOADING)
    val state: LiveData<MainState>
        get() = _state

    private val _locationFlow = locationRepository.selectedLocationFlow

    private val _location = MutableLiveData<ForecastLocation>()
    val location: LiveData<ForecastLocation>
        get() = _location

    private var onLocationCollectedJob: Job? = null

    fun startCollectingLocation() {

        viewModelScope.launch {

            _locationFlow.collect {

                when (it) {

                    is Result.Success -> {

                        _location.value = it.data
                        if (it.data.type !in arrayOf(
                                ForecastLocation.Type.LAST_KNOWN_LOCATION,
                                ForecastLocation.Type.CURRENT_LOCATION
                            )
                        ) {

                            onLoading()
                        }

                        onLocationCollectedJob?.cancel()
                        onLocationCollectedJob = viewModelScope.launch {

                            onLocationCollected(it.data)
                        }
                    }

                    is Result.Failure -> {

                        _state.value = MainState.LOCATION_ERROR
                    }
                }
            }
        }
    }

    open suspend fun onLocationCollected(location: ForecastLocation) {}

    fun onLoading() {

        _state.value = MainState.LOADING
    }

    fun onLoaded() {

        _state.value = MainState.LOADED
    }

    fun onForecastError() {

        _state.value = MainState.FORECAST_ERROR
    }
}