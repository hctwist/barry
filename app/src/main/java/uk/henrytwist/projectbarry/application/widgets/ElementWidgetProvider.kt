package uk.henrytwist.projectbarry.application.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.launch
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.resolvers.ForecastElementResolver
import uk.henrytwist.projectbarry.application.view.resolvers.TagResolver
import uk.henrytwist.projectbarry.domain.models.ForecastElement

class ElementWidgetProvider : BaseWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        appWidgetIds.forEach {

            onUpdate(context, appWidgetManager, it)
        }
    }

    fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {

        // TODO Prevent multiple forecast calls

        val views = RemoteViews(context.packageName, R.layout.element_widget)

        updateScope.launch {

            val locationOutcome = getCurrentLocation()

            if (locationOutcome is Outcome.Success) {

                val forecastOutcome = getNowForecast(locationOutcome.data)

                if (forecastOutcome is Outcome.Success) {

                    val forecast = forecastOutcome.data

                    forecast.elements.find { it is ForecastElement.UVIndex }?.let {

                        val tag = TagResolver.resolveTag(context, it)!!
                        views.setInt(R.id.element_widget_background, "setColorFilter", tag.color)

                        views.setTextViewText(R.id.element_widget_name, ForecastElementResolver.getElementTitle(context, it))
                        views.setTextViewText(R.id.element_widget_tag, tag.name)

                        appWidgetManager.updateAppWidget(widgetId, views)
                    }
                }
            }
        }
    }
}