package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.view.FragmentForecastContainer
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentCurrentForecastContainer : FragmentForecastContainer() {

    private val viewModel by viewModels<CurrentForecastViewModel>()

    override fun getFragment(): Fragment {

        return FragmentCurrentForecast()
    }

    override fun onSwipeRefresh() {

        viewModel.onSwipeRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {

            setState(it)
        }
    }
}