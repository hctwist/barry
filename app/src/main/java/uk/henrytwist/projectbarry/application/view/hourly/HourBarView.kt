package uk.henrytwist.projectbarry.application.view.hourly

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.databinding.BindingAdapter
import uk.henrytwist.projectbarry.R
import kotlin.math.max

class HourBarView(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    private val barView: View
    private val valueView: TextView

    private var minValue = 0F
    private var maxValue = 1F

    private var value = 0F

    init {

        inflate(context, R.layout.hour_bar_view, this)

        barView = getChildAt(0)
        valueView = getChildAt(1) as TextView
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {

        return MarginLayoutParams(context, attrs)
    }

    fun setRange(minValue: Float, maxValue: Float) {

        this.minValue = minValue
        this.maxValue = maxValue

        invalidate()
    }

    fun setValue(value: Float) {

        this.value = value
        invalidate()
    }

    fun setValueLabel(label: CharSequence) {

        valueView.text = label
    }

    fun setColor(color: Int) {

        barView.backgroundTintList = ColorStateList.valueOf(color)
//        valueView.setTextColor(color)
    }

    private fun getValueFraction() = if (isInEditMode) 0.4F else (value - minValue) / (maxValue - minValue)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        if (widthMode == MeasureSpec.UNSPECIFIED) {

            setMeasuredDimension(0, 0)
            return
        }

        var h = 0

        measureChildWithMargins(
                valueView,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                0
        )
        h = max(h, valueView.measuredHeight)

        val fullWidth = MeasureSpec.getSize(widthMeasureSpec)
        val remainingWidth = fullWidth - valueView.measuredWidth - valueView.marginStart

        val barWidth = (remainingWidth * getValueFraction()).toInt()
        barView.layoutParams.width = barWidth
        measureChild(barView, heightMeasureSpec, widthMeasureSpec)
        h = max(h, barView.measuredHeight)

        h = max(h, suggestedMinimumHeight)

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val offsetX = barView.layoutInternal(0)
        valueView.layoutInternal(offsetX)
    }

    // Returns the new offsetX
    private fun View.layoutInternal(offsetX: Int): Int {

        val l = offsetX + marginStart
        val t = ((this@HourBarView.height.toFloat() / 2) - (measuredHeight.toFloat() / 2)).toInt()
        val r = l + measuredWidth
        val b = t + measuredHeight

        layout(l, t, r, b)

        return r
    }

    companion object {

        @JvmStatic
        @BindingAdapter("minElementValue", "maxElementValue")
        fun bindRange(view: HourBarView, minElementValue: Double, maxElementValue: Double) {

            view.setRange(minElementValue.toFloat(), maxElementValue.toFloat())
        }
    }
}