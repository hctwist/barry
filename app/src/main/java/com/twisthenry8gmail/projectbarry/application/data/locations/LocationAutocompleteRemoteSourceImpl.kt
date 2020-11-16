package com.twisthenry8gmail.projectbarry.application.data.locations

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.Result
import com.twisthenry8gmail.projectbarry.domain.core.asSuccess
import com.twisthenry8gmail.projectbarry.domain.core.failure
import com.twisthenry8gmail.projectbarry.domain.data.locations.LocationAutocompleteRemoteSource
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationAutocompleteRemoteSourceImpl @Inject constructor(private val placesClient: PlacesClient) :
    LocationAutocompleteRemoteSource<AutocompleteSessionToken> {

    // TODO Maybe have a method that releases the autocomplete token? Then would be called from repository?
    // TODO Check pricing to see whether ^ would be beneficial

    override suspend fun autocomplete(
        query: String,
        sessionToken: AutocompleteSessionToken
    ): Result<List<LocationSearchResult>> {

        return suspendCoroutine { cont ->

            val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
                .setTypeFilter(TypeFilter.REGIONS).setSessionToken(sessionToken).build()
            placesClient.findAutocompletePredictions(request).addOnCompleteListener { response ->

                val result = response.result
                if (response.isSuccessful && result != null) {

                    val searchResults = result.autocompletePredictions.map {

                        LocationSearchResult(
                            it.placeId,
                            it.getPrimaryText(null).toString(),
                            it.getSecondaryText(null).toString()
                        )
                    }
                    cont.resume(searchResults.asSuccess())
                } else {

                    cont.resume(failure())
                }
            }
        }
    }

    override fun newSessionToken(): AutocompleteSessionToken {

        return AutocompleteSessionToken.newInstance()
    }
}