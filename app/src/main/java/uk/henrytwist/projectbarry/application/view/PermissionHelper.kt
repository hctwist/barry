package uk.henrytwist.projectbarry.application.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

object PermissionHelper {

    private const val ACCESS_FINE_LOCATION_REQUEST_CODE = 0

    fun requestLocationPermission(fragment: Fragment) {

        fragment.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ACCESS_FINE_LOCATION_REQUEST_CODE
        )
    }

    fun isLocationPermissionGranted(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {

        return requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
}