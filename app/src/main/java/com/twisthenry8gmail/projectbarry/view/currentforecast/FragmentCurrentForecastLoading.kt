package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.os.Bundle
import android.view.View
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.view.FragmentLoading

class FragmentCurrentForecastLoading : FragmentLoading() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setMessage(resources.getString(R.string.loading_forecast))
    }
}