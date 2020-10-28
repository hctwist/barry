package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.data.locations2.SavedLocationsRepository
import javax.inject.Inject

class GetMenuLocationsUseCase @Inject constructor(private val savedLocationsRepository: SavedLocationsRepository) {

    suspend operator fun invoke(): List<SavedLocation> {

        return savedLocationsRepository.getPinnedLocations()
    }
}