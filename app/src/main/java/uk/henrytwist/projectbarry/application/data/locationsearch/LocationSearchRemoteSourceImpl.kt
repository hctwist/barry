package uk.henrytwist.projectbarry.application.data.locationsearch

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.asSuccess
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRemoteSource
import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationSearchRemoteSourceImpl @Inject constructor(private val placesClient: PlacesClient) :
        LocationSearchRemoteSource<AutocompleteSessionToken> {

    // TODO Maybe have a method that releases the autocomplete token? Then would be called from repository? Check pricing to see whether it would be beneficial

    override suspend fun autocomplete(
            query: String,
            sessionToken: AutocompleteSessionToken
    ): Outcome<List<LocationSearchResult>> {

        return suspendCoroutine { cont ->

            val request = FindAutocompletePredictionsRequest.builder().setQuery(query)
                    .setTypeFilter(TypeFilter.REGIONS).setSessionToken(sessionToken).build()
            placesClient.findAutocompletePredictions(request).addOnCompleteListener { response ->

                if (response.isSuccessful && response.result != null) {

                    val searchResults = response.result.autocompletePredictions.map {

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