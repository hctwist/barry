package uk.henrytwist.projectbarry.application.view.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.ColorUtils
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.resolvers.TagResolver

open class TagView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(ContextThemeWrapper(context, R.style.Tag), attributeSet) {

    fun setTagContents(tagContents: TagContents?) {

        if (tagContents == null) {

            visibility = GONE
        } else {

            visibility = VISIBLE
            text = tagContents.name
            setColor(tagContents.color)
        }
    }

    private fun setColor(@ColorInt color: Int) {

        setTextColor(color)
        backgroundTintList = ColorStateList.valueOf(TagResolver.resolveTagBackgroundColor(color))
    }

    class TagContents(val name: String, val color: Int)
}