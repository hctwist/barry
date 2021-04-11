package uk.henrytwist.projectbarry.application.view.components.navigationview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.math.MathUtils.lerp
import uk.henrytwist.projectbarry.R
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt

class ShiftingNavigationView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    var itemSelectListener: ItemSelectListener? = null
    var adapter: ShiftingNavigationAdapter? = null

    private lateinit var selectedItem: Item

    private val items = mutableListOf<Item>()

    private var maxTitleWidth = 0
    private lateinit var itemParams: List<ItemParams2>
    private lateinit var itemParamsCapture: List<ItemParams2>
    private lateinit var itemParamsFuture: List<ItemParams2>

    private var itemTouched: Item? = null

    private val shiftAnimationDuration = 350L
    private val titleEnterAnimationDelay = 75L
    private val titleEnterAnimationDuration = 200L
    private val titleExitAnimationDuration = 150L
    private val indicatorFadeAnimationDuration = 250L
    private val shiftAnimationInterpolator = FastOutSlowInInterpolator()

    private val indicatorBackground: Drawable?
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

        val indicatorView = View(context, null)
        indicatorView.background = indicatorBackground
        indicatorView.alpha = if (firstItem) 1F else 0F
        super.addView(indicatorView, WRAP_CONTENT, WRAP_CONTENT)

        val item = Item(child.id, iconView, titleView, indicatorView)
        if (firstItem) selectedItem = item
        items.add(item)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        allocateItemParams()
    }

    private fun onItemSelected(item: Item) {

        val oldSelectedItem = selectedItem

        selectedItem = item
        itemSelectListener?.onNavigationItemSelected(item.id)
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
        }
        computeItemParams(to, itemParamsFuture)

        ValueAnimator.ofFloat(0F, 1F).run {

            duration = shiftAnimationDuration
            interpolator = shiftAnimationInterpolator
            addUpdateListener {

                val value = it.animatedValue as Float

                for (i in itemParams.indices) {

                    itemParams[i].lerp(itemParamsCapture[i], itemParamsFuture[i], value)
                }

                requestLayout()
            }
            start()
        }

        from.apply {

            titleView.animate().run {

                alpha(0F)
                startDelay = 0L
                duration = titleExitAnimationDuration
            }

            indicatorView.animate().run {

                alpha(0F)
                duration = indicatorFadeAnimationDuration
            }

            animateIconColor(iconView, false)
        }
        to.apply {

            titleView.animate().run {

                alpha(1F)
                startDelay = titleEnterAnimationDelay
                duration = titleEnterAnimationDuration
            }

            indicatorView.animate().run {

                alpha(1F)
                duration = indicatorFadeAnimationDuration
            }
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

    private fun allocateItemParams() {

        itemParams = items.map { ItemParams2() }
        itemParamsCapture = items.map { ItemParams2() }
        itemParamsFuture = items.map { ItemParams2() }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var h = 0

        maxTitleWidth = 0
        items.forEach {

            val iconSizeSpec = MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY)
            it.iconView.measure(iconSizeSpec, iconSizeSpec)

            it.titleView.measure(widthMeasureSpec, heightMeasureSpec)
            h = max(h, it.titleView.measuredHeight)
            maxTitleWidth = max(maxTitleWidth, it.titleView.measuredWidth)

            val indicatorWidth =
                it.titleView.measuredWidth + iconSize + indicatorHorizontalPadding * 2 + titlePadding
            it.indicatorView.measure(
                MeasureSpec.makeMeasureSpec(indicatorWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
            )
            Log.d(
                "ShiftingNavigationView",
                "onMeasure: ${it.indicatorView.measuredWidth}, ${it.indicatorView.width}}"
            )
        }

        val totalIconSize = iconSize * items.size
        val totalSpacing = minItemSpacing * (items.size - 1)
        val w =
            totalIconSize + totalSpacing + maxTitleWidth + titlePadding + indicatorHorizontalPadding * items.size * 2

        h = max(h, iconSize) + indicatorVerticalPadding * 2

        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        if (changed) {

            computeItemParams(selectedItem, itemParams)
        }

        val cy = (b - t) / 2

        items.forEachIndexed { index, item ->

            val itemParams = itemParams[index]

            val indicatorView = item.indicatorView
            val indicatorL = itemParams.l.roundToInt()
            val indicatorR = itemParams.r.roundToInt()
            indicatorView.layout(indicatorL, 0, indicatorR, measuredHeight)

            val icon = item.iconView
            val iconRise = icon.measuredHeight / 2
            val iconL = indicatorL + indicatorHorizontalPadding
            val iconR = iconL + icon.measuredWidth
            icon.layout(iconL, cy - iconRise, iconR, cy + iconRise)

            val titleView = item.titleView
            val titleRise = titleView.measuredHeight / 2
            val titleR = indicatorR - indicatorHorizontalPadding
            val titleL = titleR - titleView.measuredWidth
            titleView.layout(
                titleL,
                cy - titleRise,
                titleR,
                cy + titleRise
            )
        }
    }

    private fun setSelected(id: Int) {

        selectedItem.iconView.imageTintList = ColorStateList.valueOf(iconTint)
        selectedItem.titleView.alpha = 0F
        selectedItem.indicatorView.alpha = 0F

        items.find { it.id == id }?.let { selectedItem = it }
        selectedItem.iconView.imageTintList = ColorStateList.valueOf(iconTintChecked)
        selectedItem.titleView.alpha = 1F
        selectedItem.indicatorView.alpha = 1F

        computeItemParams(selectedItem, itemParams)
        requestLayout()
    }

    override fun onSaveInstanceState(): Parcelable {

        val state = SavedState(super.onSaveInstanceState())
        state.selectedItemId = selectedItem.id

        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        super.onRestoreInstanceState(state)

        if (state is SavedState) {

            setSelected(state.selectedItemId)
        }
    }

    private fun computeItemParams(
        selectedItem: Item,
        itemParams: List<ItemParams2>
    ) {

        var xOffset = 0

        val selectedTitleWidth = selectedItem.titleView.measuredWidth
        val surplusTitleWidth = maxTitleWidth - selectedTitleWidth
        val itemSpacing =
            floor(minItemSpacing + (surplusTitleWidth.toFloat() / (items.size - 1))).toInt()

        items.forEachIndexed { index, item ->

            val selected = item == selectedItem

            itemParams[index].l = xOffset.toFloat()

            val selectedOffset = if (selected) titlePadding + item.titleView.measuredWidth else 0
            xOffset += indicatorHorizontalPadding + item.iconView.measuredWidth + selectedOffset + indicatorHorizontalPadding

            itemParams[index].r = xOffset.toFloat()

            xOffset += itemSpacing
        }
    }

    class Item(
        val id: Int,
        val iconView: ImageView,
        val titleView: TextView,
        val indicatorView: View
    )

    class ItemParams2 {

        var l = 0F
        var r = 0F

        fun clone(params: ItemParams2) {

            l = params.l
            r = params.r
        }

        fun lerp(from: ItemParams2, to: ItemParams2, fraction: Float) {

            l = lerp(from.l, to.l, fraction)
            r = lerp(from.r, to.r, fraction)
        }
    }

    class SavedState : BaseSavedState {

        var selectedItemId = 0

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {

            selectedItemId = source.readInt()
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)

            out?.writeInt(selectedItemId)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun interface ItemSelectListener {

        fun onNavigationItemSelected(id: Int)
    }
}