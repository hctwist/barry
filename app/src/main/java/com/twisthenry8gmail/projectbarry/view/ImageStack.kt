package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView

class ImageStack(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    fun setImages(imageResources: Array<Int>?) {

        if (imageResources == null) {

            for (i in 0 until childCount) {

                getImageView(i).setImageDrawable(null)
            }
            return
        }

        if (childCount < imageResources.size) {

            repeat(imageResources.size - childCount) {

                addImageView()
            }
        }

        for (i in imageResources.indices) {

            getImageView(i).setImageResource(imageResources[i])
        }

        for (i in imageResources.size until childCount) {

            getImageView(i).setImageDrawable(null)
        }
    }

    private fun addImageView() {

        val v = ImageView(context)
        v.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(v)
    }

    private fun getImageView(index: Int) = getChildAt(index) as ImageView
}