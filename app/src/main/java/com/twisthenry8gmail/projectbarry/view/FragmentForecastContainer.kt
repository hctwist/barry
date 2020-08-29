package com.twisthenry8gmail.projectbarry.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twisthenry8gmail.projectbarry.MainState
import com.twisthenry8gmail.projectbarry.R
import kotlinx.android.synthetic.main.fragment_forecast_container.*
import kotlinx.android.synthetic.main.fragment_forecast_container.view.*

// TODO Replace all containers with this
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

    abstract fun getFragment(): Fragment

    open fun onSwipeRefresh() {}

    fun setState(mainState: MainState) {

        if (mainState != MainState.LOADING) {

            Log.d("FragmentForecastContain", "setState: setting swipe refresh to false")
            forecast_container_swipe_refresh.setRefreshing(false)
        }

        val state = when (mainState) {

            MainState.LOADING, MainState.LOADED -> State.CONTENT
            MainState.LOCATION_ERROR -> State.LOCATION_ERROR
            MainState.FORECAST_ERROR -> State.FORECAST_ERROR
        }

        if (childFragmentManager.findFragmentByTag(state.name) == null) {

            val fragment = when (state) {

                State.CONTENT -> getFragment()
                State.LOCATION_ERROR -> Fragment()
                State.FORECAST_ERROR -> Fragment()
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