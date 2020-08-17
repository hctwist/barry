package com.twisthenry8gmail.projectbarry.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twisthenry8gmail.projectbarry.view.StateFragmentContainerAdapter
import com.twisthenry8gmail.projectbarry.view.currentforecast.FragmentCurrentForecastContainer
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel

class MainStateAdapter(manager: FragmentManager) :
    StateFragmentContainerAdapter<MainViewModel.State>(manager) {

    override fun getFragment(state: MainViewModel.State): Fragment {

        return when (state) {

            MainViewModel.State.LOADING_LOCATION -> FragmentLoadingLocation()
            MainViewModel.State.LOCATION_ERROR -> Fragment()
            MainViewModel.State.NOW -> FragmentCurrentForecastContainer()
            MainViewModel.State.HOURLY -> Fragment()
            MainViewModel.State.DAILY -> Fragment()
        }
    }
}