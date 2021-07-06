package uk.henrytwist.projectbarry.application.widgets

import android.appwidget.AppWidgetProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import uk.henrytwist.projectbarry.domain.usecases.GetCurrentLocation
import uk.henrytwist.projectbarry.domain.usecases.GetNowForecast
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseWidgetProvider: AppWidgetProvider() {

    protected val updateScope = CoroutineScope(Dispatchers.IO)

    @Inject
    protected lateinit var getCurrentLocation: GetCurrentLocation

    @Inject
    protected lateinit var getNowForecast: GetNowForecast
}