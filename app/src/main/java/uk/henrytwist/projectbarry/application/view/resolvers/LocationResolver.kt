package uk.henrytwist.projectbarry.application.view.resolvers

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.domain.models.SelectedLocation

object LocationResolver {

    fun resolveName(context: Context, location: SelectedLocation): String? {

        return location.location?.name
    }

    fun resolveIcon(context: Context, location: SelectedLocation): Drawable {

        return ContextCompat.getDrawable(context, when (location.type) {

            SelectedLocation.Type.CURRENT_WAITING -> R.drawable.outline_gps_not_fixed_24
            SelectedLocation.Type.CURRENT -> R.drawable.outline_gps_fixed_24
            SelectedLocation.Type.CURRENT_UNAVAILABLE -> R.drawable.outline_gps_off_24
            SelectedLocation.Type.STATIC -> R.drawable.outline_place_24
        })!!
    }

    fun resolveContentDescription(context: Context, location: SelectedLocation): String {

        return context.getString(when (location.type) {

            SelectedLocation.Type.CURRENT_WAITING -> R.string.location_type_current_waiting_cd
            SelectedLocation.Type.CURRENT -> R.string.location_type_current_cd
            SelectedLocation.Type.CURRENT_UNAVAILABLE -> R.string.location_type_current_unavailable_cd
            SelectedLocation.Type.STATIC -> R.string.location_type_static_cd
        })
    }
}