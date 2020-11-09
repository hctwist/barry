package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.locations.SavedLocationsRepository
import javax.inject.Inject

class GetSavedLocationsUseCase @Inject constructor(private val savedLocationsRepository: SavedLocationsRepository) {

    suspend operator fun invoke(): List<SavedLocation> {

        return savedLocationsRepository.getLocations()
    }
}