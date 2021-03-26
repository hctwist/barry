package uk.henrytwist.projectbarry.application.view.main

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
object MainLoadingStatusResolver {

    fun resolveMessage(context: Context, loadingStatus: MainViewModel.LoadingStatus?): String {

        return when (loadingStatus) {

            // TODO
            MainViewModel.LoadingStatus.LOADING_LOCATION -> "Fetching location"
            MainViewModel.LoadingStatus.LOADING_FORECAST -> "Fetching forecast"
            else -> ""
        }
    }
}