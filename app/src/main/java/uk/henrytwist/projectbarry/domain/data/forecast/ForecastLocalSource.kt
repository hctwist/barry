package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel

interface ForecastLocalSource {

    suspend fun get(placeId: String): Outcome<ForecastModel>

    suspend fun save(forecast: ForecastModel)
}