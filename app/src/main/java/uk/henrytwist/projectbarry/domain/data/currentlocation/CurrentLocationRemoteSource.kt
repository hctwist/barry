package uk.henrytwist.projectbarry.domain.data.currentlocation

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates

interface CurrentLocationRemoteSource {

    suspend fun getLocation(): Outcome<LocationCoordinates>
}