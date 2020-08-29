package com.twisthenry8gmail.projectbarry.data.openuv

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.twisthenry8gmail.projectbarry.data.APIKeyStore
import com.twisthenry8gmail.projectbarry.core.Result
import com.twisthenry8gmail.projectbarry.core.failure
import java.time.Instant
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OpenUVRemoteSource @Inject constructor(private val volleyRequestQueue: RequestQueue) :
    OpenUVSource {

    override suspend fun getRealTimeUV(lat: Double, lng: Double) =
        suspendCoroutine<Result<OpenUVSource.RealTimeData>> { continuation ->

            volleyRequestQueue.add(OpenUVRequest(lat, lng, continuation))
        }

    class OpenUVRequest(
        lat: Double,
        lng: Double,
        continuation: Continuation<Result<OpenUVSource.RealTimeData>>
    ) : JsonObjectRequest(
        Method.GET,
        "https://api.openuv.io/api/v1/uv?lat=${lat}&lng=${lng}",
        null,
        {

            val jsonResult = it.getJSONObject("result")

            val uv = jsonResult.getDouble("uv")

            val result = OpenUVSource.RealTimeData(Instant.now().epochSecond, lat, lng, uv)

            continuation.resume(Result.Success(result))
        },
        {

            continuation.resume(failure())
        }) {

        override fun getHeaders(): MutableMap<String, String> {

            return mutableMapOf("x-access-token" to APIKeyStore.getOpenUVKey())
        }
    }
}