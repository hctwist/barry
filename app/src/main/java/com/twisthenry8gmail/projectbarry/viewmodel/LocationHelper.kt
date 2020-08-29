package com.twisthenry8gmail.projectbarry.viewmodel

import com.twisthenry8gmail.projectbarry.Result
import com.twisthenry8gmail.projectbarry.core.ForecastLocation

abstract class LocationHelper {

    private var location: ForecastLocation? = null

    suspend fun onLocationResultCollected(result: Result<ForecastLocation>) {

        if (result is Result.Success) {

            if (location?.type == ForecastLocation.Type.PENDING_LOCATION) {

                onLocationUpdated(result.data)
            } else {

                onLocationChanged(result.data)
            }

            location = result.data
        } else if (result is Result.Failure) {

            onLocationError()
        }
    }

    protected abstract suspend fun onLocationChanged(location: ForecastLocation)

    protected abstract suspend fun onLocationUpdated(location: ForecastLocation)

    protected abstract suspend fun onLocationError()
}