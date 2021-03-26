package uk.henrytwist.projectbarry.application.di

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.henrytwist.projectbarry.application.data.PremiumRepositoryImpl
import uk.henrytwist.projectbarry.application.data.settings.SettingsRepositoryImpl
import uk.henrytwist.projectbarry.domain.data.PremiumRepository
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRemoteSource
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRepository
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsLocalSource
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProviderModule {

    @Provides
    fun bindPremiumRepository(premiumRepository: PremiumRepositoryImpl): PremiumRepository = premiumRepository

    @Provides
    fun provideSettingsRepository(settingsRepository: SettingsRepositoryImpl): SettingsRepository = settingsRepository

    @Provides
    fun provideSavedLocationsRepository(savedLocationsLocalSource: SavedLocationsLocalSource): SavedLocationsRepository {

        return SavedLocationsRepository(savedLocationsLocalSource)
    }

    @Provides
    fun provideLocationAutocompleteRepository(
            locationSearchRemoteSource: LocationSearchRemoteSource<AutocompleteSessionToken>,
            geocodingRemoteSource: GeocodingRemoteSource
    ): LocationSearchRepository<*> {

        return LocationSearchRepository(locationSearchRemoteSource, geocodingRemoteSource)
    }
}