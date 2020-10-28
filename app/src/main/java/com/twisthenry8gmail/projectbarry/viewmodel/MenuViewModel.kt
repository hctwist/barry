package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.usecases.GetMenuLocationsUseCase
import com.twisthenry8gmail.projectbarry.usecases.SelectLocationUseCase
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MenuViewModel @ViewModelInject constructor(
    private val getMenuLocationsUseCase: GetMenuLocationsUseCase,
    private val selectLocationUseCase: SelectLocationUseCase
) :
    NavigatorViewModel() {

    private val _locations = MutableLiveData<List<SavedLocation>>()
    val locations: LiveData<List<SavedLocation>>
        get() = _locations

    init {

        viewModelScope.launch {

            val locations = getMenuLocationsUseCase()
            _locations.value = locations
        }
    }

    fun onSettingsClicked() {


    }

    fun onLocationSettingsClicked() {

        TODO()
//        navigateTo(R.id.action_fragmentMain_to_fragmentLocations)
    }

    fun onCurrentLocationClicked() {

        viewModelScope.launch {

            selectLocationUseCase(null)
        }
    }

    fun onLocationClicked(location: SavedLocation) {

        viewModelScope.launch {

            selectLocationUseCase(location)
        }
    }
}