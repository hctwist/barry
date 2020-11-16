package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NeedsLocationPermissionUseCase @Inject constructor(private val selectedLocationLocalSource: SelectedLocationRepository) {

    operator fun invoke(): Boolean {

        return selectedLocationLocalSource.isSelectedLocationCurrent()
    }
}