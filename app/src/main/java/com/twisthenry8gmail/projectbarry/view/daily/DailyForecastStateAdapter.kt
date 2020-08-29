package com.twisthenry8gmail.projectbarry.view.daily

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twisthenry8gmail.projectbarry.view.StateFragmentContainerAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.DailyForecastViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DailyForecastStateAdapter(manager: FragmentManager) :
    StateFragmentContainerAdapter<DailyForecastViewModel.State>(manager) {

    override fun getFragment(state: DailyForecastViewModel.State): Fragment {

        return when (state) {

            DailyForecastViewModel.State.LOADING, DailyForecastViewModel.State.LOADED -> FragmentDailyForecast()
            DailyForecastViewModel.State.ERROR -> Fragment()
        }
    }
}