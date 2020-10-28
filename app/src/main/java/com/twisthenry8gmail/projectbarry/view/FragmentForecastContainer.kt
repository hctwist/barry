package com.twisthenry8gmail.projectbarry.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.MainState
import kotlinx.android.synthetic.main.fragment_forecast_container.*
import kotlinx.android.synthetic.main.fragment_forecast_container.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class FragmentForecastContainer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_forecast_container, container, false).apply {

            forecast_container_swipe_refresh.onRefreshListener = ::onSwipeRefresh
        }
    }

    abstract fun getContentFragment(): Fragment

    abstract fun getForecastErrorFragment(): Fragment

    open fun onSwipeRefresh() {}

    fun setState(mainState: MainState) {

        if (mainState != MainState.LOADING) {

            forecast_container_swipe_refresh.setRefreshing(false)
        }

        val state = when (mainState) {

            MainState.LOADING, MainState.LOADED -> State.CONTENT
            MainState.LOCATION_ERROR -> State.LOCATION_ERROR
            MainState.FORECAST_ERROR -> State.FORECAST_ERROR
        }

        if (childFragmentManager.findFragmentByTag(state.name) == null) {

            val fragment = when (state) {

                State.CONTENT -> getContentFragment()
                State.LOCATION_ERROR -> FragmentLocationError()
                State.FORECAST_ERROR -> getForecastErrorFragment()
            }

            childFragmentManager.beginTransaction()
                .replace(R.id.forecast_container, fragment, state.name)
                .commit()
        }
    }

    enum class State {

        CONTENT, LOCATION_ERROR, FORECAST_ERROR
    }
}