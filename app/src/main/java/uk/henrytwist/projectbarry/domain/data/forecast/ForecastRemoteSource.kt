package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel
import uk.henrytwist.projectbarry.domain.models.Location
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates

interface ForecastRemoteSource {

    suspend fun get(coordinates: LocationCoordinates): Outcome<ForecastModel>
}