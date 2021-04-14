package uk.henrytwist.projectbarry.domain.data.locationsearch

import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import uk.henrytwist.kotlinbasics.outcomes.Outcome

interface LocationSearchRemoteSource<SessionToken> {

    suspend fun autocomplete(query: String, sessionToken: SessionToken): Outcome<List<LocationSearchResult>>

    fun newSessionToken(): SessionToken
}