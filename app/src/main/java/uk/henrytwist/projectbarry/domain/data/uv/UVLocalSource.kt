package uk.henrytwist.projectbarry.domain.data.uv

import uk.henrytwist.projectbarry.domain.models.UV

interface UVLocalSource {

    suspend fun get(placeId: String): UV?

    suspend fun save(uv: UV)
}