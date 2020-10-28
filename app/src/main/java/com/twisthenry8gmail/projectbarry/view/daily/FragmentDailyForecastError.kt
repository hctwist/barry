package com.twisthenry8gmail.projectbarry.view.daily

import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.view.errors.ErrorModel
import com.twisthenry8gmail.projectbarry.view.errors.FragmentError
import com.twisthenry8gmail.projectbarry.viewmodel.DailyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentDailyForecastError : FragmentError() {

    private val viewModel by viewModels<DailyForecastViewModel>(ownerProducer = { requireParentFragment() })

    override fun getErrorModel(): ErrorModel {

        return ErrorModel.forecastErrorModel(requireContext()) {

            viewModel.onRetry()
        }
    }
}