package com.twisthenry8gmail.projectbarry.view.currentforecast

import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.view.errors.ErrorModel
import com.twisthenry8gmail.projectbarry.view.errors.FragmentError
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentCurrentForecastError : FragmentError() {

    private val viewModel by viewModels<CurrentForecastViewModel>(ownerProducer = { requireParentFragment() })

    override fun getErrorModel(): ErrorModel {

        return ErrorModel.forecastErrorModel(requireContext()) {

            viewModel.onRetry()
        }
    }
}