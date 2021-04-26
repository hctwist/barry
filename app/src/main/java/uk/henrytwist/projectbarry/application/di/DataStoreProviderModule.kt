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

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProviderModule {

    @Experience
    @Provides
    fun provideExperienceDataStore(@ApplicationContext context: Context) = context.experienceDataStore

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Experience
}