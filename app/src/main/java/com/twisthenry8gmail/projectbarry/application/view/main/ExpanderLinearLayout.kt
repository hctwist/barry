package com.twisthenry8gmail.projectbarry.application.view.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import com.twisthenry8gmail.projectbarry.R
import kotlin.math.max

class ExpanderLinearLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private var adapterInternal: Adapter<*>? = null

    private val squareCells: Boolean

    init {

        context.obtainStyledAttributes(attrs, R.styleable.ExpanderLinearLayout).run {

            squareCells = getBoolean(R.styleable.ExpanderLinearLayout_squareCells, false)

            recycle()
        }
    }

    fun setAdapter(adapter: Adapter<*>) {

        adapterInternal = adapter

        removeAllViews()
        adapter.onSetTo(this)
        addViewsFrom(adapter)
    }

    private fun addViewsFrom(adapter: Adapter<*>) {

        val inflater = LayoutInflater.from(context)
        for (i in 0 until adapter.getItemCount()) {

            val box = adapter.createAndBindViewBox(i, this, inflater)

            val view = box.itemView

            addView(view)
        }
    }

    fun onAdapterInvalidated() {

        removeAllViews()
        addViewsFrom(adapterInternal!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMeasureMode != MeasureSpec.UNSPECIFIED) {

            throw RuntimeException("For height, only wrap content is supported")
        }

        // Pre measure
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var maxWidth = 0
        var maxHeight = 0

        forEach {

            if (it.measuredHeight > maxHeight) {

                maxHeight = it.measuredHeight
            }
            if (it.measuredWidth > maxWidth) {

                maxWidth = it.measuredWidth
            }
        }

        if (squareCells) {

            val maxDim = max(maxWidth, maxHeight)
            val childSpec = MeasureSpec.makeMeasureSpec(maxDim, MeasureSpec.EXACTLY)

            forEach {

                it.measure(childSpec, childSpec)
            }
        }

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(widthSize, maxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        // TODO Respect minimum margins

        if (childCount == 0) return

        if (childCount == 1) {

            val child = getChildAt(0)
            val cx = (r - l).toFloat() / 2
            val childRadius = child.measuredWidth.toFloat() / 2
            getChildAt(0).layout(
                (cx - childRadius).toInt(),
                0,
                (cx + childRadius).toInt(),
                child.measuredHeight
            )
        }

        if (squareCells) {

            val childWidth = getChildAt(0).measuredWidth

            val totalSpace = r - l - (childWidth * childCount)
            val childSpacing = totalSpace.toFloat() / (childCount - 1)

            var leftOffset = 0F
            forEach {

                val leftOffsetInt = leftOffset.toInt()
                it.layout(leftOffsetInt, 0, leftOffsetInt + it.measuredWidth, it.measuredHeight)
                leftOffset += it.measuredWidth + childSpacing
            }
        } else {

            val childLeft = getChildAt(0)
            val childRight = getChildAt(childCount - 1)

            val leftCx = l + childLeft.measuredWidth.toFloat() / 2
            val rightCx = r - childRight.measuredWidth.toFloat() / 2

            val centerSpacing = (rightCx - leftCx) / (childCount - 1)

            var cx = leftCx

            forEach {

                val childRadius = it.measuredWidth.toFloat() / 2
                it.layout(
                    (cx - childRadius).toInt(),
                    0,
                    (cx + childRadius).toInt(),
                    it.measuredHeight
                )

                cx += centerSpacing
            }
        }
    }

    abstract class Adapter<Box : ViewBox> {

        internal var observer: () -> Unit = {}

        abstract fun getItemCount(): Int

        internal fun createAndBindViewBox(
            position: Int,
            parent: ViewGroup,
            layoutInflater: LayoutInflater
        ): Box {

            val box = createViewBox(position, parent, layoutInflater)
            bindViewBox(position, box)
            return box
        }

        abstract fun createViewBox(
            position: Int,
            parent: ViewGroup,
            layoutInflater: LayoutInflater
        ): Box

        abstract fun bindViewBox(position: Int, viewBox: Box)

        fun invalidate() {

            observer()
        }

        internal fun onSetTo(expanderLinearLayout: ExpanderLinearLayout) {

            observer = {

                expanderLinearLayout.onAdapterInvalidated()
            }
        }
    }

    abstract class ViewBox(val itemView: View)
}