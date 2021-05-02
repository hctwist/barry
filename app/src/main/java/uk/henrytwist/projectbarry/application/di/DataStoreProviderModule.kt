package uk.henrytwist.projectbarry.application.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

val Context.experienceDataStore by preferencesDataStore("experience")

val Context.dataDataStore by preferencesDataStore("data")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProviderModule {

    @Experience
    @Provides
    fun provideExperienceDataStore(@ApplicationContext context: Context) = context.experienceDataStore

    @Data
    @Provides
    fun provideDataDataStore(@ApplicationContext context: Context) = context.dataDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Experience

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Data
}