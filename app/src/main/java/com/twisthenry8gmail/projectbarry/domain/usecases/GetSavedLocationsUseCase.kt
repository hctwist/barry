package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.domain.data.locations.SavedLocationsRepository
import javax.inject.Inject

class GetSavedLocationsUseCase @Inject constructor(private val savedLocationsRepository: SavedLocationsRepository) {

    suspend operator fun invoke(): List<SavedLocation> {

        return savedLocationsRepository.getLocations()
    }
}