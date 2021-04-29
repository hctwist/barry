package uk.henrytwist.projectbarry.application.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.henrytwist.projectbarry.application.data.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomProviderModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {

        return AppDatabase.build(context)
    }

    @Provides
    fun provideSavedLocationDao(appDatabase: AppDatabase) = appDatabase.savedLocationDao()

    @Provides
    fun provideForecastDao(appDatabase: AppDatabase) = appDatabase.forecastDao()
}