package uk.henrytwist.projectbarry.application.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Settings
    @Provides
    fun getSettingsPreferences(@ApplicationContext context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Data
    @Provides
    fun getDataPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.applicationContext.getSharedPreferences(
                    "data_prefs",
                    Context.MODE_PRIVATE
            )

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Settings

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Data
}