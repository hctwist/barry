package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FetchSelectedLocationUseCase @Inject constructor(private val locationRepository: ForecastLocationRepository) {

    suspend operator fun invoke() {

        locationRepository.fetchSelectedLocation()
    }
}