package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RefreshSelectedLocationUseCase @Inject constructor(private val selectedLocationRepository: SelectedLocationRepository) {

    suspend operator fun invoke() {

        selectedLocationRepository.refreshSelectedLocation()
    }
}