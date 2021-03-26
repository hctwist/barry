package uk.henrytwist.projectbarry.application.data.uv

import uk.henrytwist.projectbarry.domain.models.UV
import uk.henrytwist.projectbarry.domain.data.uv.UVLocalSource
import java.time.Instant
import javax.inject.Inject

class UVLocalSourceImpl @Inject constructor(private val dao: UVDao) : UVLocalSource {

    override suspend fun get(placeId: String): UV? {

        return dao.get(placeId)?.let {

            UV(it.placeId, Instant.ofEpochSecond(it.time), it.uv)
        }
    }

    override suspend fun save(uv: UV) {

        dao.insert(UVEntity(uv.placeId, uv.time.epochSecond, uv.uv))
    }
}