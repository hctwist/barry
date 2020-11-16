package com.twisthenry8gmail.projectbarry.application.data.locations

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.twisthenry8gmail.projectbarry.domain.data.locations.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class LocationBindingModule {

    @Binds
    abstract fun bindCurrentLocationRemoteSource(currentLocationRemoteSourceImpl: CurrentLocationRemoteSourceImpl): CurrentLocationRemoteSource

    @Binds
    abstract fun bindGeocodingRemoteSource(geocodingRemoteSourceImpl: GeocodingRemoteSourceImpl): GeocodingRemoteSource

    @Binds
    abstract fun bindLocationAutocompleteRemoteSource(locationAutocompleteRemoteSourceImpl: LocationAutocompleteRemoteSourceImpl): LocationAutocompleteRemoteSource<AutocompleteSessionToken>

    @Binds
    abstract fun bindSavedLocationsLocalSource(savedLocationsLocalSource: SavedLocationsLocalSourceImpl): SavedLocationsLocalSource

    @Binds
    abstract fun bindSelectedLocationLocalSource(selectedLocationLocalSource: SelectedLocationLocalSourceImpl): SelectedLocationLocalSource

    companion object {

        @Provides
        fun provideLocationAutocompleteRepository(
            locationAutocompleteRemoteSource: LocationAutocompleteRemoteSource<AutocompleteSessionToken>,
            geocodingRemoteSource: GeocodingRemoteSource
        ): LocationAutocompleteRepository<*> {

            return LocationAutocompleteRepository(
                locationAutocompleteRemoteSource,
                geocodingRemoteSource
            )
        }

        @Provides
        fun provideGeocodingRepository(geocodingRemoteSource: GeocodingRemoteSource): GeocodingRepository {

            return GeocodingRepository(geocodingRemoteSource)
        }

        @Provides
        fun provideSavedLocationsRepository(savedLocationsLocalSource: SavedLocationsLocalSource): SavedLocationsRepository {

            return SavedLocationsRepository(savedLocationsLocalSource)
        }

        @ExperimentalCoroutinesApi
        @Provides
        @Singleton
        fun provideSelectedLocationRepository(
            selectedLocationLocalSource: SelectedLocationLocalSource,
            savedLocationsLocalSource: SavedLocationsLocalSource,
            currentLocationRemoteSource: CurrentLocationRemoteSource,
            geocodingRemoteSource: GeocodingRemoteSource
        ): SelectedLocationRepository {

            return SelectedLocationRepository(
                selectedLocationLocalSource,
                savedLocationsLocalSource,
                currentLocationRemoteSource,
                geocodingRemoteSource
            )
        }
    }
}