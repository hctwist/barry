package com.twisthenry8gmail.projectbarry.view.main

import android.os.Bundle
import android.view.View
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.view.FragmentLoading

class FragmentLoadingLocation : FragmentLoading() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setMessage(resources.getString(R.string.loading_location))
    }
}