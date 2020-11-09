package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.uicore.LocationIcon

object LocationIconResolver {

    fun resolve(context: Context, icon: LocationIcon): Drawable? {

        return ContextCompat.getDrawable(
            context, when (icon) {

                LocationIcon.PLACE -> R.drawable.round_place_24
                LocationIcon.GPS_ON -> R.drawable.round_gps_fixed_24
                LocationIcon.GPS_OFF -> R.drawable.round_gps_not_fixed_24
            }
        )
    }
}