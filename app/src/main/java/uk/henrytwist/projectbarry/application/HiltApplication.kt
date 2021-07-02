package uk.henrytwist.projectbarry.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.henrytwist.projectbarry.domain.usecases.CleanupCache
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application() {

    @Inject
    lateinit var cleanupCache: CleanupCache

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {

            // TODO Is there a better place to put this?
            delay(2000)
            cleanupCache()
        }
    }
}