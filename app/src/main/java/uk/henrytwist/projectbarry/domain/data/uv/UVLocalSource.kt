package uk.henrytwist.projectbarry.domain.data.uv

import uk.henrytwist.projectbarry.domain.models.UV

@Deprecated("Replaced by OpenWeather API")
interface UVLocalSource {

    suspend fun get(placeId: String): UV?

    suspend fun save(uv: UV)
}