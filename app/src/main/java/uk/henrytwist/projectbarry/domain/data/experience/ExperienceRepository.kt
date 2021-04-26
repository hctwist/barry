package uk.henrytwist.projectbarry.domain.data.experience

interface ExperienceRepository {

    suspend fun hasCompletedOnboarding(): Boolean

    suspend fun setHasCompletedOnboarding()
}