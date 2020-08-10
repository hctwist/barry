package com.twisthenry8gmail.projectbarry.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier

@Module
@InstallIn(ApplicationComponent::class)
object SharedPreferencesModule {

    @Settings
    @Provides
    fun getSettingsPreferences(@ApplicationContext context: Context) =
        context.applicationContext.getSharedPreferences(
            "settings_prefs",
            Context.MODE_PRIVATE
        )

    @Data
    @Provides
    fun getDataPreferences(@ApplicationContext context: Context) =
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