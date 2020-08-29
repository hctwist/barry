package com.twisthenry8gmail.projectbarry.view.daily

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.viewmodel.DailyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentDailyContainer : Fragment(R.layout.fragment_daily_container) {

    private val stateAdapter by lazy {
        DailyForecastStateAdapter(
            childFragmentManager
        )
    }

    private val viewModel by viewModels<DailyForecastViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner) {

            stateAdapter.setState(it)
        }

        stateAdapter.attachTo(view)
    }
}