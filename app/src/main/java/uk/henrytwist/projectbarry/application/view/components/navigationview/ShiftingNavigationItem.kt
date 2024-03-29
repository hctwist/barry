package uk.henrytwist.projectbarry.application.view.components.navigationview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import uk.henrytwist.projectbarry.R

class ShiftingNavigationItem(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val title: String?
    val icon: Drawable?

    init {

        context.obtainStyledAttributes(attrs, R.styleable.ShiftingNavigationItem).run {

            title = getString(R.styleable.ShiftingNavigationItem_android_text)
            icon = getDrawable(R.styleable.ShiftingNavigationItem_android_icon)
            recycle()
        }
    }
}