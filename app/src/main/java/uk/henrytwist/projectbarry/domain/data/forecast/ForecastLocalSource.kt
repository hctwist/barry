package uk.henrytwist.projectbarry.domain.data.forecast

import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.application.data.forecast.ForecastModel
import uk.henrytwist.projectbarry.domain.models.LocationCoordinates
import java.time.Instant

interface ForecastLocalSource {

    suspend fun get(afterTime: Instant, coordinates: LocationCoordinates, distanceTolerance: Double): Outcome<ForecastModel>

    suspend fun save(forecast: ForecastModel)

    suspend fun delete(beforeTime: Instant)
}