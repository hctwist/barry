package uk.henrytwist.projectbarry.domain.data.uv

import uk.henrytwist.kotlinbasics.Outcome
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.UV

@Deprecated("Replaced by OpenWeather API")
interface UVRemoteSource {

    suspend fun get(location: Location): Outcome<UV>
}