package uk.henrytwist.projectbarry.application.view.components

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import uk.henrytwist.projectbarry.R

class LoadingView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    init {

        inflate(context, R.layout.loading_view, this)
    }

    fun setMessage(message: CharSequence) {

        findViewById<TextView>(R.id.loading_view_message).text = message
    }
}