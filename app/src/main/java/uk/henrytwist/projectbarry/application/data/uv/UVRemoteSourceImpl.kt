package uk.henrytwist.projectbarry.application.data.uv

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.kotlinbasics.outcomes.failure
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.data.APIKeyStore
import uk.henrytwist.projectbarry.domain.models.UV
import uk.henrytwist.projectbarry.domain.data.uv.UVRemoteSource
import java.time.Instant
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UVRemoteSourceImpl @Inject constructor(private val volleyRequestQueue: RequestQueue) :
        UVRemoteSource {

    override suspend fun get(location: Location): Outcome<UV> {

        return suspendCoroutine { continuation ->

            volleyRequestQueue.add(OpenUVRequest(location, continuation))
        }
    }

    class OpenUVRequest(
            location: Location,
            continuation: Continuation<Outcome<UV>>
    ) : JsonObjectRequest(
            Method.GET,
            "https://api.openuv.io/api/v1/uv?lat=${location.coordinates.lat}&lng=${location.coordinates.lng}",
            null,
            {

                val jsonResult = it.getJSONObject("result")

                val uv = jsonResult.getDouble("uv")

                val result = UV(location.placeId, Instant.now(), uv)

                continuation.resume(Outcome.Success(result))
            },
            {

                continuation.resume(failure())
            }) {

        override fun getHeaders(): MutableMap<String, String> {

            return mutableMapOf("x-access-token" to APIKeyStore.getOpenUVKey())
        }
    }
}