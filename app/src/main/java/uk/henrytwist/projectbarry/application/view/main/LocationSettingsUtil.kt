package uk.henrytwist.projectbarry.application.view.main

import android.content.Context
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

object LocationSettingsUtil {

    fun isLocationEnabled(context: Context, callback: (Boolean, ResolvableApiException?) -> Unit) {

        val locationRequest = LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val settingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
        LocationServices.getSettingsClient(context).checkLocationSettings(settingsRequest).addOnCompleteListener {

            callback(it.isSuccessful && it.result?.locationSettingsStates?.isLocationUsable == true, it.exception as? ResolvableApiException)
        }
    }
}