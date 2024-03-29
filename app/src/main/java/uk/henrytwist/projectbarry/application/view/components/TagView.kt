package uk.henrytwist.projectbarry.application.view.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import uk.henrytwist.projectbarry.R

// TODO Is this theme causing the flash problem?
open class TagView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(ContextThemeWrapper(context, R.style.Tag), attributeSet) {

    private var tagContents: TagContents? = null

    fun setTagContents(tagContents: TagContents?) {

        this.tagContents = tagContents
        if (tagContents == null) {

            visibility = GONE
        } else {

            text = tagContents.name
            backgroundTintList = ColorStateList.valueOf(tagContents.color)
            visibility = VISIBLE
        }
    }

    override fun setVisibility(visibility: Int) {

        when (visibility) {

            VISIBLE -> if (tagContents != null) super.setVisibility(visibility)
            INVISIBLE, GONE -> super.setVisibility(visibility)
        }
    }

    class TagContents(val name: String, val color: Int)
}