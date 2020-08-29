package com.twisthenry8gmail.projectbarry.view.daily

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.view.FragmentForecastContainer
import com.twisthenry8gmail.projectbarry.viewmodel.DailyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentDailyContainer : FragmentForecastContainer() {

    private val viewModel by viewModels<DailyForecastViewModel>()

    override fun getFragment(): Fragment {

        return FragmentDailyForecast()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner) {

            setState(it)
        }
    }
}