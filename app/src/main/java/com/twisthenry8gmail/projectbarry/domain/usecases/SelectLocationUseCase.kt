package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.data.locations.SelectedLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@Deprecated("Replaced by SelectMenuLocationUseCase")
@ExperimentalCoroutinesApi
class SelectLocationUseCase @Inject constructor(private val selectedLocationRepository: SelectedLocationRepository) {

    suspend operator fun invoke(location: SavedLocation?) {

        if (location == null) {

            selectedLocationRepository.selectCurrentLocation()
        } else {

//            selectedLocationRepository.select(location)
        }
    }
}