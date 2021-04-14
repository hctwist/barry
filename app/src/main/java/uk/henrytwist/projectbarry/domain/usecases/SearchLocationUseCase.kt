package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRepository
import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(private val searchRepository: LocationSearchRepository<*>) {

    suspend operator fun invoke(query: String): Outcome<List<LocationSearchResult>> {

        return searchRepository.autocomplete(query)
    }
}