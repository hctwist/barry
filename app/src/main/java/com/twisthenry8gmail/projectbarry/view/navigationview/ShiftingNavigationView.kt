package com.twisthenry8gmail.projectbarry.view.navigationview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.math.MathUtils.lerp
import com.twisthenry8gmail.projectbarry.R
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt

typealias ShiftingNavigationItemSelected = (id: Int) -> Unit

class ShiftingNavigationView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    var onItemSelected: ShiftingNavigationItemSelected? = null
    var adapter: ShiftingNavigationAdapter? = null

    private lateinit var selectedItem: Item

    private val items = mutableListOf<Item>()

    private var indicatorView: ImageView? = null

    private var maxTitleWidth = 0
    private lateinit var itemParams: List<ItemParams>
    private lateinit var itemParamsCapture: List<ItemParams>
    private lateinit var itemParamsFuture: List<ItemParams>

    private val indicatorParams = IndicatorParams()
    private val indicatorParamsCapture = IndicatorParams()
    private val indicatorParamsFuture = IndicatorParams()

    private var itemTouched: Item? = null

    private val shiftAnimationDuration = 250L
    private val titleAnimationDuration = 100L
    private val shiftAnimationInterpolator = FastOutSlowInInterpolator()

    private val textAppearanceRes: Int
    private val iconTint: Int
    private val iconTintChecked: Int
    private val iconSize: Int
    private val titlePadding =
        resources.getDimensionPixelSize(R.dimen.shifting_navigation_title_padding)
    private val indicatorHorizontalPadding: Int
    private val indicatorVerticalPadding: Int
    private val minItemSpacing: Int
    private val minTouchSize =
        resources.getDimensionPixelSize(R.dimen.shifting_navigation_min_touch_size)

    init {

        val indicatorBackground: Drawable?
        context.obtainStyledAttributes(attrs, R.styleable.ShiftingNavigationView).run {

            indicatorBackground =
                getDrawable(R.styleable.ShiftingNavigationView_shiftingIndicatorBackground)
            indicatorHorizontalPadding = getDimensionPixelSize(
                R.styleable.ShiftingNavigationView_shiftingIndicatorHorizontalPadding,
                resources.getDimensionPixelSize(R.dimen.shifting_navigation_indicator_horizontal_padding)
            )
            indicatorVerticalPadding = getDimensionPixelSize(
                R.styleable.ShiftingNavigationView_shiftingIndicatorVerticalPadding,
                resources.getDimensionPixelSize(R.dimen.shifting_navigation_indicator_vertical_padding)
            )

            iconSize = getDimensionPixelSize(
                R.styleable.ShiftingNavigationView_shiftingIconSize,
                resources.getDimensionPixelSize(R.dimen.shifting_navigation_icon)
            )
            iconTint = getColor(R.styleable.ShiftingNavigationView_shiftingIconTint, Color.BLACK)
            iconTintChecked =
                getColor(R.styleable.ShiftingNavigationView_shiftingIconTintChecked, iconTint)

            minItemSpacing = getDimensionPixelSize(
                R.styleable.ShiftingNavigationView_shiftingIconMinSpacing,
                resources.getDimensionPixelSize(R.dimen.shifting_navigation_spacing)
            )

            textAppearanceRes = getResourceId(
                R.styleable.ShiftingNavigationView_android_textAppearance,
                R.style.TextAppearance_AppCompat
            )

            recycle()
        }

        if (indicatorBackground != null) {

            indicatorView = ImageView(context).apply {

                setImageDrawable(indicatorBackground)
            }
            super.addView(indicatorView)
        }
    }

    override fun addView(child: View?, params: LayoutParams?) {
        addViewInternal(child)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return when (event?.action) {

            MotionEvent.ACTION_DOWN -> {

                itemTouched = getItemAt(event.x, event.y)
                if (itemTouched != selectedItem) itemTouched?.let { onItemTouched(it) }
                true
            }

            MotionEvent.ACTION_UP -> {

                itemTouched?.let {

                    if (it != selectedItem) {

                        val itemReleased = getItemAt(event.x, event.y)
                        if (it == itemReleased) {

                            onItemSelected(it)
                        } else {

                            onItemReleased(it)
                        }
                    }
                }
                true
            }

            MotionEvent.ACTION_CANCEL -> {

                if (itemTouched != selectedItem) itemTouched?.let { onItemReleased(it) }
                true
            }

            else -> false
        }
    }

    private fun getItemAt(x: Float, y: Float): Item? {

        val touchOutset = max(minTouchSize, iconSize).toFloat() / 2
        items.forEach {

            val iconCX = it.iconView.x + it.iconView.width.toFloat() / 2
            val iconCY = it.iconView.y + it.iconView.height.toFloat() / 2
            if (x in (iconCX - touchOutset)..(iconCX + touchOutset) && y in (iconCY - touchOutset)..(iconCY + touchOutset)) {

                return it
            }
        }

        return null
    }

    private fun addViewInternal(child: View?) {

        if (child !is ShiftingNavigationItem) {

            throw RuntimeException("Any children must be of type ${ShiftingNavigationItem::class.simpleName}")
        }

        val firstItem = items.isEmpty()

        val iconView = ImageView(context)
        iconView.imageTintList = if (firstItem) {

            ColorStateList.valueOf(iconTintChecked)
        } else {

            ColorStateList.valueOf(iconTint)
        }
        iconView.setImageDrawable(child.icon)
        super.addView(iconView, iconSize, iconSize)

        val titleView = TextView(context)
        titleView.setTextAppearance(textAppearanceRes)
        titleView.text = child.title
        titleView.alpha = if (firstItem) 1F else 0F
        super.addView(titleView, WRAP_CONTENT, WRAP_CONTENT)

        val item = Item(child.id, iconView, titleView)
        if (firstItem) selectedItem = item
        items.add(item)
    }

    private fun onItemSelected(item: Item) {

        val oldSelectedItem = selectedItem

        selectedItem = item
        onItemSelected?.invoke(item.id)
        adapter?.onItemSelected(item.id)

        startItemSelectedAnimation(oldSelectedItem, item)
    }

    private fun onItemTouched(item: Item) {

        animateIconColor(item.iconView, true)
    }

    private fun onItemReleased(item: Item) {

        animateIconColor(item.iconView, false)
    }

    private fun startItemSelectedAnimation(from: Item, to: Item) {

        for (i in itemParams.indices) {

            itemParamsCapture[i].clone(itemParams[i])
            indicatorParamsCapture.clone(indicatorParams)
        }
        computeItemParams(to, itemParamsFuture, indicatorParamsFuture)

        val animator = ValueAnimator.ofFloat(0F, 1F).run {

            duration = shiftAnimationDuration
            interpolator = shiftAnimationInterpolator
            addUpdateListener {

                val value = it.animatedValue as Float

                for (i in itemParams.indices) {

                    itemParams[i].lerp(itemParamsCapture[i], itemParamsFuture[i], value)
                }

                indicatorParams.lerp(indicatorParamsCapture, indicatorParamsFuture, value)

                requestLayout()
            }
            start()
        }

        from.apply {

            titleView.animate().alpha(0F).duration = titleAnimationDuration
            animateIconColor(iconView, false)
        }
        to.apply {

            titleView.animate().alpha(1F).duration = titleAnimationDuration
        }
    }

    private fun animateIconColor(icon: ImageView, checked: Boolean) {

        icon.imageTintList?.let { currentTintList ->

            val currentTint =
                currentTintList.getColorForState(intArrayOf(), currentTintList.defaultColor)

            ValueAnimator.ofArgb(currentTint, if (checked) iconTintChecked else iconTint).run {

                duration = shiftAnimationDuration
                addUpdateListener {

                    val value = it.animatedValue as Int
                    icon.imageTintList = ColorStateList.valueOf(value)
                }
                start()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        allocateItemParams()
    }

    private fun allocateItemParams() {

        itemParams = items.map { ItemParams() }
        itemParamsCapture = items.map { ItemParams() }
        itemParamsFuture = items.map { ItemParams() }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var h = 0

        maxTitleWidth = 0
        items.forEach {

            h = max(h, it.titleView.measuredHeight)
            maxTitleWidth = max(maxTitleWidth, it.titleView.measuredWidth)
        }

        val totalIconSize = iconSize * items.size
        val totalSpacing = minItemSpacing * (items.size - 1)
        val w =
            totalIconSize + totalSpacing + maxTitleWidth + titlePadding + indicatorHorizontalPadding * items.size * 2

        h = max(h, iconSize) + indicatorVerticalPadding * 2

        setMeasuredDimension(w, h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        if (changed) {

            computeItemParams(selectedItem, itemParams, indicatorParams)
        }

        val cy = (b - t) / 2

        items.forEachIndexed { index, item ->

            val itemParams = itemParams[index]

            val icon = item.iconView
            val iconRise = icon.measuredHeight / 2
            val iconL = itemParams.xOffset.roundToInt()
            val iconR = iconL + icon.measuredWidth
            icon.layout(iconL, cy - iconRise, iconR, cy + iconRise)

            val titleView = item.titleView
            val titleRise = titleView.measuredHeight / 2
            val titleL = iconR + titlePadding
            val titleR = titleL + titleView.measuredWidth
            titleView.layout(
                titleL,
                cy - titleRise,
                titleR,
                cy + titleRise
            )

            indicatorView?.layout(
                indicatorParams.l.roundToInt(),
                0,
                indicatorParams.r.roundToInt(),
                measuredHeight
            )
        }
    }

    private fun computeItemParams(
        selectedItem: Item,
        itemParams: List<ItemParams>,
        indicatorParams: IndicatorParams
    ) {

        var xOffset = 0

        val selectedTitleWidth = selectedItem.titleView.measuredWidth
        val surplusTitleWidth = maxTitleWidth - selectedTitleWidth
        val itemSpacing =
            floor(minItemSpacing + (surplusTitleWidth.toFloat() / (items.size - 1))).toInt()

        items.forEachIndexed { index, item ->

            val selected = item == selectedItem

            xOffset += indicatorHorizontalPadding
            itemParams[index].xOffset = xOffset.toFloat()

            if (selected) indicatorParams.l = xOffset.toFloat() - indicatorHorizontalPadding

            xOffset += item.iconView.measuredWidth

            if (selected) xOffset += titlePadding + item.titleView.measuredWidth

            xOffset += indicatorHorizontalPadding

            if (selected) indicatorParams.r = xOffset.toFloat()

            xOffset += itemSpacing
        }
    }

    class Item(
        val id: Int,
        val iconView: ImageView,
        val titleView: TextView
    )

    class ItemParams {

        var xOffset = 0F

        fun clone(params: ItemParams) {

            xOffset = params.xOffset
        }

        fun lerp(from: ItemParams, to: ItemParams, fraction: Float) {

            xOffset = lerp(from.xOffset, to.xOffset, fraction)
        }
    }

    class IndicatorParams {

        var l = 0F
        var r = 0F

        fun clone(params: IndicatorParams) {

            l = params.l
            r = params.r
        }

        fun lerp(from: IndicatorParams, to: IndicatorParams, fraction: Float) {

            l = lerp(from.l, to.l, fraction)
            r = lerp(from.r, to.r, fraction)
        }
    }
}