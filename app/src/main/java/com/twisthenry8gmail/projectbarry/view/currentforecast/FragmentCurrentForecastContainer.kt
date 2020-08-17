package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentCurrentForecastContainer : Fragment(R.layout.fragment_current_forecast_container) {

    private val stateAdapter by lazy {
        CurrentForecastStateAdapter(
            childFragmentManager
        )
    }

    private val viewModel by viewModels<CurrentForecastViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner, Observer {

            stateAdapter.setState(it)
        })

        stateAdapter.attachTo(view)
    }
}