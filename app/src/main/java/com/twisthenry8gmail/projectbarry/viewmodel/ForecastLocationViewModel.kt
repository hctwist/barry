package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.core.LocationData
import com.twisthenry8gmail.projectbarry.core.MainState
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.usecases.GetSelectedLocationFlowUseCase
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class ForecastLocationViewModel(getSelectedLocationFlowUseCase: GetSelectedLocationFlowUseCase) :
    NavigatorViewModel() {

    private val _state = MutableLiveData(MainState.LOADING)
    val state: LiveData<MainState>
        get() = _state

    private val _locationFlow = getSelectedLocationFlowUseCase()

    private val _location = MutableLiveData<SelectedLocation>()
    val location: LiveData<SelectedLocation>
        get() = _location

    private var onLocationCollectedJob: Job? = null

    fun startCollectingLocation() {

        viewModelScope.launch {

            _locationFlow.collect { selectedLocation ->

                when (selectedLocation) {

                    is Result.Success -> {

                        _location.value = selectedLocation.data

                        val status = selectedLocation.data.status

                        if (status != SelectedLocation.Status.CURRENT_LOCATION) {

                            onLoading()
                        }

                        selectedLocation.data.locationData?.also {

                            onLocationCollectedJob?.cancel()
                            onLocationCollectedJob = viewModelScope.launch {

                                onLocationCollected(it)
                            }
                        }
                    }

                    is Result.Failure -> {

                        _state.value = MainState.LOCATION_ERROR
                    }
                }
            }
        }
    }

    open suspend fun onLocationCollected(location: LocationData) {}

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