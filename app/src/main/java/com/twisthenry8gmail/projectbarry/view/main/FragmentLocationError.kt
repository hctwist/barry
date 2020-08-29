package com.twisthenry8gmail.projectbarry.view.main

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.view.FragmentError

class FragmentLocationError : FragmentError() {

    override fun getErrorImageRes(): Int {

        return 0
    }

    override fun getErrorMessage(): String {

        return getString(R.string.location_error)
    }

    override fun getErrorButtonText(): String {

        return getString(R.string.location_retry)
    }

    override fun onErrorButtonClicked() {
        TODO("Not yet implemented")
    }
}