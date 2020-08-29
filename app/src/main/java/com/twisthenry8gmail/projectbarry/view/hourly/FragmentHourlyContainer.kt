package com.twisthenry8gmail.projectbarry.view.hourly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.twisthenry8gmail.projectbarry.MainState
import com.twisthenry8gmail.projectbarry.view.FragmentForecastContainer
import com.twisthenry8gmail.projectbarry.viewmodel.HourlyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentHourlyContainer : FragmentForecastContainer() {

    private val viewModel by viewModels<HourlyForecastViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner, Observer {

            setState(MainState.LOADED)
        })
    }

    override fun getFragment(): Fragment {

        return FragmentHourly()
    }
}