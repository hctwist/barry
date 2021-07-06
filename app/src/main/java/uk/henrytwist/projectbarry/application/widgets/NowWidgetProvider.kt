package uk.henrytwist.projectbarry.application.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.resolvers.ForecastResolver
import uk.henrytwist.projectbarry.application.view.resolvers.WeatherConditionResolver
import uk.henrytwist.projectbarry.domain.usecases.GetCurrentLocation
import uk.henrytwist.projectbarry.domain.usecases.GetNowForecast
import javax.inject.Inject


@AndroidEntryPoint
class NowWidgetProvider : BaseWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        appWidgetIds.forEach {

            onUpdate(context, appWidgetManager, it)
        }
    }

    fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {

        val views = RemoteViews(context.packageName, R.layout.now_widget)

        updateScope.launch {

            val locationOutcome = getCurrentLocation()

            if (locationOutcome is Outcome.Success) {

                val forecastOutcome = getNowForecast(locationOutcome.data)

                if (forecastOutcome is Outcome.Success) {

                    val forecast = forecastOutcome.data

                    views.setImageViewResource(R.id.now_widget_icon, WeatherConditionResolver.resolveIconResource(forecast.condition, forecast.isNight)!!)
                    views.setTextViewText(R.id.now_widget_temperature, ForecastResolver.displayTemperature(context, forecast.temp))
                    views.setTextViewText(R.id.now_widget_location, locationOutcome.data.name)
                    views.setTextViewText(R.id.now_widget_condition, WeatherConditionResolver.resolveName(context, forecast.condition))

                    appWidgetManager.updateAppWidget(widgetId, views)
                }
            }
        }
    }
}