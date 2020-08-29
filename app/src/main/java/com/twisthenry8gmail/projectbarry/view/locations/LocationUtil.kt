package com.twisthenry8gmail.projectbarry.view.locations

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.ForecastLocation

object LocationUtil {

    fun resolveIconRes(type: ForecastLocation.Type) = when (type) {

        ForecastLocation.Type.PENDING_LOCATION -> R.drawable.round_gps_not_fixed_24
        ForecastLocation.Type.CURRENT_LOCATION -> R.drawable.round_gps_fixed_24
        ForecastLocation.Type.LAST_KNOWN_LOCATION -> R.drawable.round_gps_off_24
        ForecastLocation.Type.CHOSEN, ForecastLocation.Type.PINNED -> R.drawable.round_place_24
    }
}