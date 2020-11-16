package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import com.twisthenry8gmail.projectbarry.domain.uicore.MenuLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectMenuLocationUseCase @Inject constructor(private val selectedLocationRepository: SelectedLocationRepository) {

    suspend operator fun invoke(location: MenuLocation) {

        when (location) {

            is MenuLocation.Current -> selectedLocationRepository.selectCurrentLocation()
            is MenuLocation.Saved -> selectedLocationRepository.select(location.id)
        }
    }
}