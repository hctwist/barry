package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.data.locations2.ForecastLocationRepository2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectLocationUseCase @Inject constructor(private val locationRepository: ForecastLocationRepository2) {

    suspend operator fun invoke(location: SavedLocation?) {

        if (location == null) {

            locationRepository.selectCurrentLocation()
        } else {

            locationRepository.select(location)
        }
    }
}