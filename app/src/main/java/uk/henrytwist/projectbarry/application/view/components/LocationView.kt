package uk.henrytwist.projectbarry.application.view.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.resolvers.LocationResolver
import uk.henrytwist.projectbarry.domain.models.SelectedLocation

class LocationView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context, attributeSet) {

    private val nameView: TextView
    private val iconView: ImageView

    init {

        inflate(context, R.layout.location_view, this)

        nameView = findViewById(R.id.location_name)
        iconView = findViewById(R.id.location_icon)
    }

    fun setLocation(location: SelectedLocation?) {

        if (location != null) {

            nameView.text = LocationResolver.resolveName(context, location)
            iconView.setImageDrawable(LocationResolver.resolveIcon(context, location))
        }
    }
}