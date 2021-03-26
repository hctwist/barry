package uk.henrytwist.projectbarry.application.view.locationpermission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.kotlinbasics.Trigger
import uk.henrytwist.projectbarry.R

class LocationPermissionViewModel : NavigatorViewModel() {

    private val _locationPermissionRequest = MutableLiveData<Trigger>()
    val locationPermissionRequest: LiveData<Trigger>
        get() = _locationPermissionRequest

    fun onGrantPermissionClicked() {

        _locationPermissionRequest.value = Trigger()
    }

    fun onLocationPermissionResult(granted: Boolean) {

        if (granted) {

            navigate(R.id.action_fragmentLocationPermission_to_fragmentMain2)
        }
    }

    fun onOtherLocationClicked() {

        navigate(R.id.action_fragmentLocationPermission_to_menuFragment)
    }
}