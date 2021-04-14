package uk.henrytwist.projectbarry.application.view.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import uk.henrytwist.projectbarry.R

class ElementTag(context: Context, attributeSet: AttributeSet) : AppCompatTextView(ContextThemeWrapper(context, R.style.ElementTag), attributeSet) {

    fun setElementTag(tag: Tag?) {

        if (tag == null) {

            visibility = GONE
        } else {

            visibility = VISIBLE
            setText(tag.nameRes)
            setColor(ContextCompat.getColor(context, tag.colorRes))
        }
    }

    private fun setColor(@ColorInt color: Int) {

        setTextColor(color)
        backgroundTintList = ColorStateList.valueOf(MaterialColors.compositeARGBWithAlpha(color, 32))
    }

    class Tag(@StringRes val nameRes: Int, @ColorRes val colorRes: Int)
}