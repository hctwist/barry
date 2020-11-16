package com.twisthenry8gmail.projectbarry.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.domain.core.Trigger
import com.twisthenry8gmail.projectbarry.application.viewmodel.navigator.NavigatorViewModel

class LocationPermissionViewModel : NavigatorViewModel() {

    private val _locationPermissionRequest = MutableLiveData<Trigger>()
    val locationPermissionRequest: LiveData<Trigger>
        get() = _locationPermissionRequest

    fun onGrantPermissionClicked() {

        _locationPermissionRequest.value = Trigger()
    }

    fun onLocationPermissionResult(granted: Boolean) {

        if (granted) {

            navigateTo(R.id.action_fragmentLocationPermission_to_fragmentMain2)
        }
    }
}