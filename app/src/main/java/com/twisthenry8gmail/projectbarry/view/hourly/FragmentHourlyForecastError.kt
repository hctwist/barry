package com.twisthenry8gmail.projectbarry.view.hourly

import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.view.errors.ErrorModel
import com.twisthenry8gmail.projectbarry.view.errors.FragmentError
import com.twisthenry8gmail.projectbarry.viewmodel.HourlyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentHourlyForecastError : FragmentError() {

    private val viewModel by viewModels<HourlyForecastViewModel>(ownerProducer = { requireParentFragment() })

    override fun getErrorModel(): ErrorModel {

        return ErrorModel.forecastErrorModel(requireContext()) {

            viewModel.onRetry()
        }
    }
}