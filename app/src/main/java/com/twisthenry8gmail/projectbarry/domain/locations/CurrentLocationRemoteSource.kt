package com.twisthenry8gmail.projectbarry.domain.locations

import com.twisthenry8gmail.projectbarry.core.Result

interface CurrentLocationRemoteSource {

    suspend fun getLastLocation(): Result<CurrentLocation>

    suspend fun getLocationUpdate(): Result<CurrentLocation>
}