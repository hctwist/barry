package uk.henrytwist.projectbarry.application.data.experience

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import uk.henrytwist.projectbarry.application.di.DataStoreProviderModule
import uk.henrytwist.projectbarry.domain.data.experience.ExperienceRepository
import javax.inject.Inject

class ExperienceRepositoryImpl @Inject constructor(@DataStoreProviderModule.Experience private val store: DataStore<Preferences>) : ExperienceRepository {

    private val onboardingKey = booleanPreferencesKey("completed_onboarding")

    override suspend fun hasCompletedOnboarding(): Boolean {

        return store.data.first()[onboardingKey] ?: false
    }

    override suspend fun setHasCompletedOnboarding() {

        store.edit { it[onboardingKey] = true }
    }
}