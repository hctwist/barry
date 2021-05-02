package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import javax.inject.Inject

class CleanupCache @Inject constructor(private val forecastRepository: ForecastRepository) {

    suspend operator fun invoke() {

        forecastRepository.cleanup()
    }
}