package com.twisthenry8gmail.projectbarry.view.currentforecast

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twisthenry8gmail.projectbarry.view.StateFragmentContainerAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

// TODO Refactor to 'Now'
//@ExperimentalCoroutinesApi
//class CurrentForecastStateAdapter(
//    manager: FragmentManager
//) :
//    StateFragmentContainerAdapter<CurrentForecastViewModel.State>(manager) {
//
//    override fun getFragment(state: CurrentForecastViewModel.State): Fragment {
//
//        return when (state) {
//
//            CurrentForecastViewModel.State.LOADING, CurrentForecastViewModel.State.LOADED -> FragmentCurrentForecast()
//            CurrentForecastViewModel.State.ERROR -> Fragment()
//        }
//    }
//}