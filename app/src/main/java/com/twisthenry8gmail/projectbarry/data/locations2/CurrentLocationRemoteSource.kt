package com.twisthenry8gmail.projectbarry.data.locations2

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.failure
import com.twisthenry8gmail.projectbarry.core.success
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias LocationUpdateListener = (Result<Location>) -> Unit

class CurrentLocationRemoteSource @Inject constructor(private val locationClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Result<Location> {

        return suspendCoroutine { cont ->

            locationClient.lastLocation.addOnCompleteListener {

                try {
                    val result = it.result

                    if (it.isSuccessful && result != null) {

                        cont.resume(success(result))
                    } else {

                        cont.resume(failure())
                    }
                } catch (e: Exception) {

                    cont.resume(failure())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocationUpdate(): Result<Location> {

        val request = LocationRequest.create().apply {

            numUpdates = 1
            interval = 4000L
        }

        return suspendCoroutine {

            locationClient.requestLocationUpdates(
                request,
                object : LocationCallback() {

                    override fun onLocationAvailability(availability: LocationAvailability?) {

                        if (availability?.isLocationAvailable != true) {

                            locationClient.removeLocationUpdates(this)
                            it.resume(failure())
                        }
                    }

                    override fun onLocationResult(result: LocationResult?) {

                        locationClient.removeLocationUpdates(this)
                        if (result != null) {

                            it.resume(success(result.lastLocation))
                        } else {

                            it.resume(failure())
                        }
                    }
                },
                Looper.getMainLooper()
            )
        }
    }
}