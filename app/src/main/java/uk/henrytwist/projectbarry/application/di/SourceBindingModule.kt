package uk.henrytwist.projectbarry.application.di

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.henrytwist.projectbarry.application.data.currentlocation.CurrentLocationRemoteSourceImpl
import uk.henrytwist.projectbarry.application.data.forecast.ForecastLocalSourceImpl
import uk.henrytwist.projectbarry.application.data.forecast.ForecastRemoteSourceImpl
import uk.henrytwist.projectbarry.application.data.geocoding.GeocodingRemoteSourceImpl
import uk.henrytwist.projectbarry.application.data.locationsearch.LocationSearchRemoteSourceImpl
import uk.henrytwist.projectbarry.application.data.savedlocations.SavedLocationsLocalSourceImpl
import uk.henrytwist.projectbarry.application.data.selectedlocation.SelectedLocationLocalSourceImpl
import uk.henrytwist.projectbarry.application.data.uv.UVLocalSourceImpl
import uk.henrytwist.projectbarry.application.data.uv.UVRemoteSourceImpl
import uk.henrytwist.projectbarry.domain.data.currentlocation.CurrentLocationRemoteSource
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastLocalSource
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRemoteSource
import uk.henrytwist.projectbarry.domain.data.geocoding.GeocodingRemoteSource
import uk.henrytwist.projectbarry.domain.data.locationsearch.LocationSearchRemoteSource
import uk.henrytwist.projectbarry.domain.data.savedlocations.SavedLocationsLocalSource
import uk.henrytwist.projectbarry.domain.data.selectedlocation.SelectedLocationLocalSource
import uk.henrytwist.projectbarry.domain.data.uv.UVLocalSource
import uk.henrytwist.projectbarry.domain.data.uv.UVRemoteSource

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceBindingModule {

    @Binds
    abstract fun bindCurrentLocationRemoteSource(currentLocationRemoteSource: CurrentLocationRemoteSourceImpl): CurrentLocationRemoteSource

    @Binds
    abstract fun bindGeocodingRemoteSource(geocodingRemoteSource: GeocodingRemoteSourceImpl): GeocodingRemoteSource

    @Binds
    abstract fun bindLocationSearchRemoteSource(locationSearchRemoteSource: LocationSearchRemoteSourceImpl): LocationSearchRemoteSource<AutocompleteSessionToken>

    @Binds
    abstract fun bindSavedLocationsLocalSource(savedLocationsLocalSource: SavedLocationsLocalSourceImpl): SavedLocationsLocalSource

    @Binds
    abstract fun bindSelectedLocationLocalSource(selectedLocationLocalSource: SelectedLocationLocalSourceImpl): SelectedLocationLocalSource

    @Binds
    abstract fun bindForecastLocalSource(source: ForecastLocalSourceImpl): ForecastLocalSource

    @Binds
    abstract fun bindForecastRemoteSource(source: ForecastRemoteSourceImpl): ForecastRemoteSource

    @Binds
    abstract fun bindUVLocalSource(source: UVLocalSourceImpl): UVLocalSource

    @Binds
    abstract fun bindUVRemoteSource(source: UVRemoteSourceImpl): UVRemoteSource
}