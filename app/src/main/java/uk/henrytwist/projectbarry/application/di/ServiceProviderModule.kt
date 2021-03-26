package uk.henrytwist.projectbarry.application.di

import android.content.Context
import android.content.res.Resources
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.henrytwist.projectbarry.domain.data.APIKeyStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceProviderModule {

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun buildVolleyRequestQueue(@ApplicationContext context: Context): RequestQueue {

        return Volley.newRequestQueue(context.applicationContext)
    }

    @Provides
    @Singleton
    fun createPlacesClient(@ApplicationContext context: Context): PlacesClient {

        Places.initialize(context, APIKeyStore.getPlacesKey())
        return Places.createClient(context.applicationContext)
    }

    @Provides
    @Singleton
    fun createFusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient {

        return LocationServices.getFusedLocationProviderClient(context.applicationContext)
    }
}