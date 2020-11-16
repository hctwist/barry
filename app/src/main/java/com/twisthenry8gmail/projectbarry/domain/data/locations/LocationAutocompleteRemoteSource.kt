package com.twisthenry8gmail.projectbarry.domain.data.locations

import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.Result

interface LocationAutocompleteRemoteSource<SessionToken> {

    suspend fun autocomplete(query: String, sessionToken: SessionToken): Result<List<LocationSearchResult>>

    fun newSessionToken(): SessionToken
}