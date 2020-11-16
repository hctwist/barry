package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.data.locations.SavedLocationsRepository
import javax.inject.Inject

class TogglePinLocationUseCase @Inject constructor(private val savedLocationsRepository: SavedLocationsRepository) {

    suspend operator fun invoke(location: SavedLocation) {

        savedLocationsRepository.pinLocation(location, !location.pinned)
    }
}