package uk.henrytwist.projectbarry.application.view.main

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import uk.henrytwist.androidbasics.getColorAttr
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.TimeDisplayUtil
import java.time.ZonedDateTime

class SunriseSunsetView(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    private val icons: Array<ImageView>
    private val line: View
    private val dots: Array<ImageView>
    private val labels: Array<TextView>

    init {

        inflate(context, R.layout.phase_view, this)

        icons = Array(2) { getChildAt(it) as ImageView }
        line = getChildAt(2)
        dots = Array(2) { getChildAt(3 + it) as ImageView }
        labels = Array(2) { getChildAt(5 + it) as TextView }
    }

    fun setSunriseSunset(sunrise: ZonedDateTime?, sunset: ZonedDateTime?) {

        setLabel(labels[0], sunrise)
        setLabel(labels[1], sunset)

        val highlightSunrise = sunrise?.let { ZonedDateTime.now().isBefore(it) } == true
        val highlightSunset = !highlightSunrise && (sunset?.let { ZonedDateTime.now().isBefore(it) } == true)

        setHighlight(icons[0], dots[0], highlightSunrise)
        setHighlight(icons[1], dots[1], highlightSunset)
    }

    private fun setLabel(label: TextView, time: ZonedDateTime?) {

        label.text = time?.let { TimeDisplayUtil.displayTime(it) }
    }

    private fun setHighlight(icon: ImageView, dot: ImageView, highlight: Boolean) {

        val iconColor: Int
        val dotColor: Int
        if (highlight) {

            val colorPrimary = context.getColorAttr(android.R.attr.colorPrimary)
            iconColor = colorPrimary
            dotColor = colorPrimary
        }
        else {

            iconColor = context.getColorAttr(android.R.attr.colorControlNormal)
            dotColor = ContextCompat.getColor(context, R.color.phase_view_dot)
        }
        icon.imageTintList = ColorStateList.valueOf(iconColor)
        dot.imageTintList = ColorStateList.valueOf(dotColor)
    }

    companion object {

        @BindingAdapter("app:sunrise", "app:sunset")
        @JvmStatic
        fun sunriseSunset(sunriseSunsetView: SunriseSunsetView, sunrise: ZonedDateTime?, sunset: ZonedDateTime?) {

            sunriseSunsetView.setSunriseSunset(sunrise, sunset)
        }
    }
}