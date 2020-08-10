package com.twisthenry8gmail.projectbarry

import android.Manifest
import androidx.fragment.app.Fragment

object PermissionsHelper {

    const val ACCESS_FINE_LOCATION_REQUEST = 0
}

fun Fragment.requestLocationPermission() {

    requestPermissions(
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        PermissionsHelper.ACCESS_FINE_LOCATION_REQUEST
    )
}