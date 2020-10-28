package com.twisthenry8gmail.projectbarry.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.Trigger
import com.twisthenry8gmail.projectbarry.viewmodel.navigator.NavigatorViewModel

class LocationPermissionViewModel : NavigatorViewModel() {

    private val _locationPermissionRequest = MutableLiveData<Trigger>()
    val locationPermissionRequest: LiveData<Trigger>
        get() = _locationPermissionRequest

    fun onGrantPermissionClicked() {

        _locationPermissionRequest.value = Trigger()
    }

    fun onLocationPermissionResult(granted: Boolean) {

        if (granted) {

            TODO()
//            navigateTo(R.id.action_fragmentLocationPermission_to_fragmentMain)
        }
    }
}