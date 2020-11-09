package com.twisthenry8gmail.projectbarry.data.locations

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.*
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.asSuccess
import com.twisthenry8gmail.projectbarry.core.failure
import com.twisthenry8gmail.projectbarry.domain.locations.CurrentLocation
import com.twisthenry8gmail.projectbarry.domain.locations.CurrentLocationRemoteSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CurrentLocationRemoteSourceImpl(private val locationClient: FusedLocationProviderClient) :
    CurrentLocationRemoteSource {

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): Result<CurrentLocation> {

        return suspendCoroutine { cont ->

            locationClient.lastLocation.addOnCompleteListener {

                try {
                    val result = it.result

                    if (it.isSuccessful && result != null) {

                        cont.resume(CurrentLocation(result.latitude, result.longitude).asSuccess())
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
    override suspend fun getLocationUpdate(): Result<CurrentLocation> {

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

                            val lastLocation = result.lastLocation
                            it.resume(
                                CurrentLocation(
                                    lastLocation.latitude,
                                    lastLocation.longitude
                                ).asSuccess()
                            )
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