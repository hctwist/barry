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

    fun resolveIcon(context: Context, location: SelectedLocation): Drawable? {

        return ContextCompat.getDrawable(context, when {

            location.isCurrent && location.location == null -> R.drawable.outline_gps_not_fixed_24
            location.isCurrent -> R.drawable.outline_gps_fixed_24
            else -> R.drawable.outline_place_24
        })
    }
}