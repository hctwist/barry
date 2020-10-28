package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.data.locations2.ForecastLocationRepository2
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetSelectedLocationFlowUseCase @Inject constructor(private val locationRepository: ForecastLocationRepository2) {

    operator fun invoke(): StateFlow<Result<SelectedLocation>> {

        return locationRepository.selectedLocation
    }
}