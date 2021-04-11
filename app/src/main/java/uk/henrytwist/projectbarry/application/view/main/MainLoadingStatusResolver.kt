package uk.henrytwist.projectbarry.application.view.main

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.projectbarry.R

@ExperimentalCoroutinesApi
object MainLoadingStatusResolver {

    fun resolveMessage(context: Context, status: MainViewModel.LoadingStatus?): String? {

        return when (status) {

            MainViewModel.LoadingStatus.LOADING_LOCATION -> context.getString(R.string.main_loading_location)
            MainViewModel.LoadingStatus.LOADING_FORECAST -> context.getString(R.string.main_loading_forecast)
            else -> null
        }
    }
}