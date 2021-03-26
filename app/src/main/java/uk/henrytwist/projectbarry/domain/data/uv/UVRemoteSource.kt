package uk.henrytwist.projectbarry.domain.data.uv

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.UV

interface UVRemoteSource {

    suspend fun get(location: Location): Outcome<UV>
}