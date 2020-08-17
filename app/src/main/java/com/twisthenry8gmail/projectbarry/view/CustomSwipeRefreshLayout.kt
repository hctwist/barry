package com.twisthenry8gmail.projectbarry.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import com.twisthenry8gmail.projectbarry.R

class CustomSwipeRefreshLayout(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs) {

    var onRefreshListener: (() -> Unit)? = null

    private lateinit var refreshView: View
    private lateinit var contentView: View

    private val fadeAmount = 0.8F
    private val overDragHeight = resources.getDimension(R.dimen.main_refresh_over_drag)

    private val dragInterpolator = DecelerateInterpolator()

    private val settleAnimationDuration = 250L
    private val settleInterpolator = OvershootInterpolator()

    private val returnAnimationDuration = 200L
    private val returnInterpolator = DecelerateInterpolator()

    private var downY = 0F
    private var dragAmount = 0F
    private var state =
        UIState.STATIC
    private var returnAfterSettle = false

    fun setRefreshing(refreshing: Boolean) {

        if (refreshing) {

            // TODO
        } else {

            when (state) {

                UIState.REFRESHING -> onReturn()

                UIState.SETTLING -> returnAfterSettle = true

                else -> {
                }
            }
        }
    }

    override fun onFinishInflate() {

        super.onFinishInflate()

        require(childCount == 2) {

            "This view must have two direct children, one for the refreshing view and one for the content"
        }

        refreshView = getChildAt(0)
        contentView = getChildAt(1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        refreshView.translationY = -refreshView.measuredHeight.toFloat()
    }


    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {

        return when (event?.action) {

            MotionEvent.ACTION_DOWN -> {

                downY = event.y
                false
            }

            MotionEvent.ACTION_MOVE -> {

                onDrag(event.y)
                state == UIState.DRAGGING
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

                if (state == UIState.DRAGGING) {

                    onRelease()
                    true
                } else false
            }

            else -> false
        }
    }

    // TODO Accessibility
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (state != UIState.STATIC && state != UIState.DRAGGING) return false

        return when (event?.action) {

            MotionEvent.ACTION_DOWN -> true

            MotionEvent.ACTION_MOVE -> {

                onDrag(event.y)
                state == UIState.DRAGGING
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

                onRelease()
                false
            }

            else -> false
        }
    }

    private fun onDrag(y: Float) {

        val drag = y - downY

        if (drag > 0) {

            state =
                UIState.DRAGGING

            val maxDrag = refreshView.height + overDragHeight
            dragAmount =
                dragInterpolator.getInterpolation((drag / maxDrag).coerceAtMost(1F)) * maxDrag
            invalidateDrag()
        }
    }

    private fun invalidateDrag() {

        refreshView.translationY = dragAmount - refreshView.height

        contentView.translationY = dragAmount
        contentView.alpha =
            1 - fadeAmount * (dragAmount / refreshView.height.toFloat()).coerceAtMost(1F)
    }

    private fun onRelease() {

        if (dragAmount > refreshView.height) {

            onRefreshListener?.invoke()
            onSettle()
        } else {

            onReturn()
        }
    }

    private fun onSettle() {

        state =
            UIState.SETTLING

        ValueAnimator.ofFloat(dragAmount, refreshView.height.toFloat()).run {

            duration = settleAnimationDuration
            interpolator = settleInterpolator
            addUpdateListener {

                dragAmount = it.animatedValue as Float
                invalidateDrag()
            }
            doOnEnd {

                if (returnAfterSettle) {

                    returnAfterSettle = false
                    onReturn()
                } else {

                    onRefresh()
                }
            }
            start()
        }
    }

    private fun onRefresh() {

        state =
            UIState.REFRESHING
    }

    private fun onReturn() {

        state =
            UIState.RETURNING

        ValueAnimator.ofFloat(dragAmount, 0F).run {

            duration = returnAnimationDuration
            interpolator = returnInterpolator
            addUpdateListener {

                dragAmount = it.animatedValue as Float
                invalidateDrag()
            }
            doOnEnd {

                state =
                    UIState.STATIC
            }
            start()
        }
    }

    private enum class UIState {

        STATIC, DRAGGING, SETTLING, REFRESHING, RETURNING
    }
}