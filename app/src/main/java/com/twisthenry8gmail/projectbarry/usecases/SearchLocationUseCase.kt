package com.twisthenry8gmail.projectbarry.usecases

import com.twisthenry8gmail.projectbarry.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchLocationUseCase @Inject constructor(private val locationRepository: ForecastLocationRepository) {

    suspend operator fun invoke(query: String): Result<List<LocationSearchResult>> {

        return locationRepository.search(query)
    }
}