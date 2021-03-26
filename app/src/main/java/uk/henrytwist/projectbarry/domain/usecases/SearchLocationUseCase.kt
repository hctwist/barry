package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.kotlinbasics.Outcome
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(private val searchRepository: LocationSearchRepository<*>) {

    suspend operator fun invoke(query: String): Outcome<List<LocationSearchResult>> {

        return searchRepository.autocomplete(query)
    }
}