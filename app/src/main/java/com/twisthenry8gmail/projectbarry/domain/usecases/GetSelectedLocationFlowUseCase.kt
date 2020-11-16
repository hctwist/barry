package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.Result
import com.twisthenry8gmail.projectbarry.domain.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetSelectedLocationFlowUseCase @Inject constructor(private val selectedLocationRepository: SelectedLocationRepository) {

    operator fun invoke(): StateFlow<Result<SelectedLocation>> {

        return selectedLocationRepository.selectedLocation
    }
}