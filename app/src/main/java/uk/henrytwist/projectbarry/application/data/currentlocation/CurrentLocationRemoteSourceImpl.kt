package uk.henrytwist.projectbarry.application.data.currentlocation

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.withTimeoutOrNull
import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.kotlinbasics.asSuccess
import uk.henrytwist.kotlinbasics.failure
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRemoteSource
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CurrentLocationRemoteSourceImpl @Inject constructor(
        private val locationClient: FusedLocationProviderClient
) : CurrentLocationRemoteSource {

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): Outcome<LocationCoordinates> {

        return suspendCoroutine { cont ->

            locationClient.lastLocation.addOnCompleteListener {

                try {
                    val result = it.result

                    if (it.isSuccessful && result != null) {

                        cont.resume(LocationCoordinates(result.latitude, result.longitude).asSuccess())
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
    override suspend fun getLocationUpdate(): Outcome<LocationCoordinates> {

        return withTimeoutOrNull(8000) {

            val request = LocationRequest.create().apply {

                numUpdates = 1
                interval = 4000L
            }

            suspendCoroutine {

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
                                    it.resume(LocationCoordinates(
                                            lastLocation.latitude,
                                            lastLocation.longitude
                                    ).asSuccess())
                                } else {

                                    it.resume(failure())
                                }
                            }
                        },
                        Looper.getMainLooper()
                )
            }
        } ?: failure()
    }
}