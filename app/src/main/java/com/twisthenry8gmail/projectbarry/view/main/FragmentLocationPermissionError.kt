package com.twisthenry8gmail.projectbarry.view.main

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.view.FragmentError
import com.twisthenry8gmail.projectbarry.view.PermissionHelper

class FragmentLocationPermissionError : FragmentError() {

    override fun getErrorImageRes(): Int {

        return 0
    }

    override fun getErrorMessage(): String {

        return getString(R.string.location_permission_error)
    }

    override fun getErrorButtonText(): String {

        return getString(R.string.location_permission_allow)
    }

    override fun onErrorButtonClicked() {

        PermissionHelper.requestLocationPermission(requireParentFragment())
    }
}