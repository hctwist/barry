package com.twisthenry8gmail.projectbarry.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twisthenry8gmail.projectbarry.view.StateFragmentContainerAdapter
import com.twisthenry8gmail.projectbarry.view.currentforecast.FragmentCurrentForecastContainer
import com.twisthenry8gmail.projectbarry.view.daily.FragmentDailyContainer
import com.twisthenry8gmail.projectbarry.view.hourly.FragmentHourlyContainer
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainStateAdapter(manager: FragmentManager) :
    StateFragmentContainerAdapter<MainViewModel.State>(manager) {

    override fun getFragment(state: MainViewModel.State): Fragment? {

        return when (state) {

            MainViewModel.State.WAITING -> null
            MainViewModel.State.NOW -> FragmentCurrentForecastContainer()
            MainViewModel.State.HOURLY -> FragmentHourlyContainer()
            MainViewModel.State.DAILY -> FragmentDailyContainer()
        }
    }
}