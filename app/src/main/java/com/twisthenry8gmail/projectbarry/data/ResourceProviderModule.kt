package com.twisthenry8gmail.projectbarry.data

import android.content.Context
import androidx.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocationEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ResourceProviderModule {

    @Provides
    @Singleton
    fun createRoomModel(@ApplicationContext context: Context): RoomModel {

        return Room.databaseBuilder(context, RoomModel::class.java, "barry_db").build()
    }

    @Provides
    fun getForecastLocationDao(roomModel: RoomModel): ForecastLocationEntity.Dao {

        return roomModel.forecastLocationDao()
    }

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