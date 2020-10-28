package com.twisthenry8gmail.projectbarry.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twisthenry8gmail.projectbarry.core.MainState
import com.twisthenry8gmail.projectbarry.view.StateFragmentContainerAdapter
import com.twisthenry8gmail.projectbarry.view.forecast.FragmentForecast
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainStateAdapter(manager: FragmentManager) :
    StateFragmentContainerAdapter<MainState>(manager) {

    override fun getFragment(state: MainState): Fragment? {

        return when (state) {

            MainState.LOADING -> null
            MainState.LOCATION_ERROR -> null
            MainState.FORECAST_ERROR -> null
            MainState.LOADED -> FragmentForecast()
        }
    }
}