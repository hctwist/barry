package uk.henrytwist.projectbarry.application.view.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import uk.henrytwist.projectbarry.R
import kotlin.math.max

class ExpanderLinearLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private var adapterInternal: Adapter<*>? = null

    private val squareCells: Boolean
    private val minMargins: Float

    init {

        context.obtainStyledAttributes(attrs, R.styleable.ExpanderLinearLayout).run {

            squareCells = getBoolean(R.styleable.ExpanderLinearLayout_squareCells, false)
            minMargins = getDimension(R.styleable.ExpanderLinearLayout_minMargins, 0F)

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

            // TODO This gets triggered every now and again even though the height is wrap_content
//            throw RuntimeException("For height, only wrap content is supported")
        }

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        // Pre measure
//        measureChildren(widthMeasureSpec, heightMeasureSpec)

//        var maxWidth = 0
//        var maxHeight = 0
//
//        forEach {
//
//            if (it.measuredHeight > maxHeight) {
//
//                maxHeight = it.measuredHeight
//            }
//            if (it.measuredWidth > maxWidth) {
//
//                maxWidth = it.measuredWidth
//            }
//        }

        val cellWidth = ((widthSize - minMargins * (childCount - 1)) / childCount).toInt()

        var maxHeight = 0

        if (squareCells) {

//            val maxDim = max(maxWidth, maxHeight)
//            val childSpec = MeasureSpec.makeMeasureSpec(maxDim, MeasureSpec.EXACTLY)

            val childSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.EXACTLY)

            forEach {

                it.measure(childSpec, childSpec)
            }

            maxHeight = cellWidth
        } else {

            val childWidthSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.AT_MOST)

            forEach {

                val childHeight = it.layoutParams.height
                it.measure(childWidthSpec, getChildMeasureSpec(heightMeasureSpec, 0, childHeight))
                maxHeight = max(maxHeight, it.measuredHeight)
            }
        }

        setMeasuredDimension(widthSize, resolveSize(maxHeight, heightMeasureSpec))
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

            // TODO This is very primitive - but is it just better?

            val childLeft = getChildAt(0)
            val childRight = getChildAt(childCount - 1)

            val childLeftCenter = childLeft.measuredWidth.toFloat() / 2
            val childRightCenter = (r - l) - childRight.measuredWidth.toFloat() / 2

            val centerSpacing = (childRightCenter - childLeftCenter) / (childCount - 1)

            forEachIndexed { i, child ->

                val center = childLeftCenter + centerSpacing * i
                val childRadius = child.measuredRadius()
                child.layout(
                    (center - childRadius).toInt(),
                    0,
                    (center + childRadius).toInt(),
                    child.measuredHeight
                )
            }
        }
    }

    private fun alg2(l: Int, r: Int) {

        val n = childCount
        val spaceEach = ((r - l).toFloat() / n).toInt()

        val wms1 = MeasureSpec.makeMeasureSpec(spaceEach, MeasureSpec.AT_MOST)
        val hms = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        forEach {

            it.measure(wms1, hms)
        }

        val spaceRemaining = getChildAt(0).measuredWidth
    }

    private fun alg(l: Int, t: Int, r: Int, b: Int) {

        // TODO Consider 1 or 2 children

        // Compute centers
        val centers = Array(childCount) { 0F }
        computeCenters(l, r, centers)

        // First pass to measure left and right overlaps
        val childLeftOverlap = computeOverlap(centers, 0, 1)
        val childRightOverlap = computeOverlap(centers, childCount - 2, childCount - 1)
    }

    private fun computeCenters(l: Int, r: Int, centers: Array<Float>) {

        val childLeft = getChildAt(0)
        val childRight = getChildAt(childCount - 1)

        centers[0] = l + childLeft.measuredWidth.toFloat() / 2
        centers[childCount - 1] = r - childRight.measuredWidth.toFloat() / 2

        val spacing = (centers[childCount - 1] - centers[0]) / (childCount - 1)
        for (i in 1 until childCount - 1) {

            centers[i] = centers[i - 1] + spacing
        }
    }

    private fun computeOverlap(centers: Array<Float>, i1: Int, i2: Int): Float {

        ensureOrdered(i1, i2)

        return centers[i1] + getChildAt(i1).measuredWidth.toFloat() / 2 -
                (centers[i2] - getChildAt(i2).measuredWidth.toFloat() / 2)
    }

    private fun shrinkSide(l: Int, r: Int, centers: Array<Float>, leftSide: Boolean) {

        val sideI = if (leftSide) 0 else childCount - 1
        val companionI = if (leftSide) 1 else childCount - 2

        val side = getChildAt(sideI)
        val companion = getChildAt(companionI)

        val dSize = side.measuredWidth - companion.measuredWidth

        if (dSize > 0) {

            var newSideRadius: Float

            val companionRadius = companion.measuredRadius()

            newSideRadius = if (leftSide) {
                // Shrink side
                (centers[childCount - 1] - l - companionRadius * (childCount - 1)) / childCount
            } else {

                (centers[0] - r + companionRadius * (childCount - 1)) / childCount
            }

            if (newSideRadius < companionRadius) {

                newSideRadius = companionRadius
            }

            // Re-measure side
            side.measure(
                MeasureSpec.makeMeasureSpec(
                    (newSideRadius * 2).toInt(),
                    MeasureSpec.AT_MOST
                ), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )

            // Recompute centres
            computeCenters(l, r, centers)
        } else {

            TODO("Shrink companion")
        }
    }

    private fun View.measuredRadius() = measuredWidth.toFloat() / 2

    private fun ensureOrdered(i1: Int, i2: Int) {

        if (i1 > i2) throw RuntimeException("i1 must be smaller than i2")
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