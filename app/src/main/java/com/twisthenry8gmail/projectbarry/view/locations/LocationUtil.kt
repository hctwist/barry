package com.twisthenry8gmail.projectbarry.view.locations

import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation

object LocationUtil {

    fun resolveIconRes(type: ForecastLocation.Type) = when (type) {

        ForecastLocation.Type.CURRENT_LOCATION -> R.drawable.round_my_location_24
        ForecastLocation.Type.CHOSEN, ForecastLocation.Type.PINNED -> R.drawable.round_place_24
    }
}