package com.twisthenry8gmail.projectbarry.view.errors

import android.content.Context
import android.graphics.drawable.Drawable
import com.twisthenry8gmail.projectbarry.R

class ErrorModel(
    val image: Drawable?,
    val title: String,
    val message: String,
    val buttonText: String,
    val errorButtonClickListener: () -> Unit
) {

    companion object {

        fun forecastErrorModel(context: Context, errorButtonClickListener: () -> Unit) = ErrorModel(
            null,
            context.getString(R.string.forecast_error_title),
            context.getString(R.string.forecast_error_message),
            context.getString(R.string.retry),
            errorButtonClickListener
        )

        fun locationErrorModel(context: Context, errorButtonClickListener: () -> Unit) = ErrorModel(
            null,
            context.getString(R.string.location_error_title),
            context.getString(R.string.location_error_message),
            context.getString(R.string.retry),
            errorButtonClickListener
        )
    }
}