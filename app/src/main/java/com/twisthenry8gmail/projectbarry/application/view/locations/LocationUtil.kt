package com.twisthenry8gmail.projectbarry.application.view.locations

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.domain.core.SelectedLocation

object LocationUtil {

    fun resolveIconRes(type: SelectedLocation.Status) = when (type) {

        SelectedLocation.Status.INTERMEDIATE_CURRENT_LOCATION -> R.drawable.round_gps_not_fixed_24
        SelectedLocation.Status.CURRENT_LOCATION -> R.drawable.round_gps_fixed_24
        SelectedLocation.Status.OUTDATED_CURRENT_LOCATION -> R.drawable.round_gps_off_24
        SelectedLocation.Status.STATIC_LOCATION -> R.drawable.round_place_24
    }
}