package com.twisthenry8gmail.projectbarry.domain.usecases

import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.Result
import com.twisthenry8gmail.projectbarry.domain.data.locations.LocationAutocompleteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchLocationUseCase @Inject constructor(private val autocompleteRepository: LocationAutocompleteRepository<*>) {

    suspend operator fun invoke(query: String): Result<List<LocationSearchResult>> {

        return autocompleteRepository.autocomplete(query)
    }
}