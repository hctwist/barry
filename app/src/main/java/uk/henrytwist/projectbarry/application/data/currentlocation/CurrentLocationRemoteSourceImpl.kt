package uk.henrytwist.projectbarry.application.data.currentlocation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
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

    @Deprecated("Not needed?")
    @SuppressLint("MissingPermission")
    suspend fun isLocationAvailable(): Boolean {

        Log.v("Barry", "Checking availability")

        return suspendCoroutine { continuation ->

            locationClient.locationAvailability.addOnCompleteListener {

                if (!it.isSuccessful || !it.result.isLocationAvailable) {

                    Log.v("Barry", "Not available")
                    continuation.resume(false)
                } else {

                    Log.v("Barry", "Available")
                    continuation.resume(true)
                }
            }
        }
    }

    override suspend fun getLocation(): Outcome<LocationCoordinates> {

        val lastLocation = getLastLocation()

        val location = if (lastLocation is Outcome.Success && lastLocation.data.accuracy < 100) {

            lastLocation
        } else {

            getCurrentLocation()
        }

        return location.map {

            LocationCoordinates(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Outcome<Location> {

        return suspendCancellableCoroutine { continuation ->

            val cancellationSource = CancellationTokenSource()
            continuation.invokeOnCancellation { cancellationSource.cancel() }

            // TODO getLastLocation first? Does this already try a cache?
            locationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, cancellationSource.token).addOnCompleteListener {

                val result = it.result

                if (it.isSuccessful && result != null) {

                    continuation.resume(result.asSuccess())
                } else {

                    continuation.resume(failure())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Outcome<Location> {

        return suspendCoroutine { cont ->

            locationClient.lastLocation.addOnCompleteListener {

                try {
                    val result = it.result

                    if (it.isSuccessful && result != null) {

                        cont.resume(result.asSuccess())
                    } else {

                        cont.resume(failure())
                    }
                } catch (e: Exception) {

                    cont.resume(failure())
                }
            }
        }
    }

    @Deprecated("Replaced with getCurrentLocation")
    @SuppressLint("MissingPermission")
    suspend fun getLocationUpdate(): Outcome<LocationCoordinates> {

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