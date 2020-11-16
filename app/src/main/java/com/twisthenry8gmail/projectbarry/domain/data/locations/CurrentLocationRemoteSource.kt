package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.Result

interface CurrentLocationRemoteSource {

    suspend fun getLastLocation(): Result<CurrentLocation>

    suspend fun getLocationUpdate(): Result<CurrentLocation>
}