package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.locations2.ForecastLocationRepository2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FetchSelectedLocationUseCase @Inject constructor(private val locationRepository: ForecastLocationRepository2) {

    suspend operator fun invoke() {

        locationRepository.fetchSelectedLocation()
    }
}