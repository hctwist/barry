package com.twisthenry8gmail.projectbarry.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.view.currentforecast.FragmentCurrentForecast
import com.twisthenry8gmail.projectbarry.view.navigationview.ShiftingNavigationAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainNavigationAdapter(manager: FragmentManager) :
    ShiftingNavigationAdapter(R.id.main_container, manager) {

    override fun getFragment(id: Int): Fragment {

        return when (id) {

            R.id.main_navigation_now -> FragmentCurrentForecast()
            R.id.main_navigation_hourly -> Fragment()
            R.id.main_navigation_daily -> Fragment()
            else -> Fragment()
        }
    }
}