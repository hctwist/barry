package uk.henrytwist.projectbarry.application.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.resolvers.ForecastResolver
import uk.henrytwist.projectbarry.application.view.resolvers.WeatherConditionResolver


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

                    views.setViewVisibility(R.id.now_widget_loading, View.GONE)

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