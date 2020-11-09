package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domainservices.MenuLocationsService
import com.twisthenry8gmail.projectbarry.uicore.MenuLocation
import com.twisthenry8gmail.projectbarry.usecases.SelectLocationUseCase
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MenuViewModel @ViewModelInject constructor(
    private val menuLocationsService: MenuLocationsService,
    private val selectLocationUseCase: SelectLocationUseCase
) : NavigatorViewModel() {

    val locations = liveData {

        emit(menuLocationsService.getMenuLocations())
    }

    fun onSettingsClicked() {


    }

    fun onLocationSettingsClicked() {

        navigateTo(R.id.action_fragmentMain2_to_fragmentLocations)
    }

    fun onLocationClicked(location: MenuLocation) {

        viewModelScope.launch {

            // TODO
        }
    }
}