package com.twisthenry8gmail.projectbarry.application.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.application.viewmodel.navigator.NavigatorViewModel
import com.twisthenry8gmail.projectbarry.domain.uicore.MenuLocation
import com.twisthenry8gmail.projectbarry.domain.usecases.GetMenuLocationsUseCase
import com.twisthenry8gmail.projectbarry.domain.usecases.SelectMenuLocationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MenuViewModel @ViewModelInject constructor(
    private val getMenuLocationsUseCase: GetMenuLocationsUseCase,
    private val selectLocationUseCase: SelectMenuLocationUseCase
) : NavigatorViewModel() {

    val locations = liveData {

        emit(getMenuLocationsUseCase.getMenuLocations())
    }

    fun onSettingsClicked() {


    }

    fun onLocationSettingsClicked() {

        navigateTo(R.id.action_fragmentMain2_to_fragmentLocations)
    }

    fun onLocationClicked(location: MenuLocation) {

        viewModelScope.launch {

            selectLocationUseCase(location)
        }
    }
}