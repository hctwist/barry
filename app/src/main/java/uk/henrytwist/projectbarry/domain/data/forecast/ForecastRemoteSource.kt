package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel
import uk.henrytwist.projectbarry.domain.models.Location

interface ForecastRemoteSource {

    suspend fun get(location: Location): Outcome<ForecastModel>
}