package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.experience.ExperienceRepository
import uk.henrytwist.projectbarry.domain.models.ScaledSpeed
import uk.henrytwist.projectbarry.domain.models.ScaledTemperature
import javax.inject.Inject

class CompleteOnboarding @Inject constructor(
        private val experienceRepository: ExperienceRepository,
        private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(temperatureScale: ScaledTemperature.Scale?, speedScale: ScaledSpeed.Scale?) {

        temperatureScale?.let { settingsRepository.setTemperatureScale(it) }
        speedScale?.let { settingsRepository.setSpeedScale(speedScale) }
        experienceRepository.setHasCompletedOnboarding()
    }
}