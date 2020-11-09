package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetSelectedLocationFlowUseCase @Inject constructor(private val locationRepository: ForecastLocationRepository) {

    operator fun invoke(): StateFlow<Result<SelectedLocation>> {

        return locationRepository.selectedLocation
    }
}