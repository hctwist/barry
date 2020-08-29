package com.twisthenry8gmail.projectbarry.view.hourly

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.ForecastElement
import com.twisthenry8gmail.projectbarry.core.WeatherCondition
import com.twisthenry8gmail.projectbarry.util.TimeDisplayUtil
import com.twisthenry8gmail.projectbarry.view.ForecastDisplayUtil
import com.twisthenry8gmail.projectbarry.view.WeatherConditionDisplay
import kotlinx.android.synthetic.main.hourly_forecast_view_hour.view.*
import java.time.ZonedDateTime
import kotlin.math.max

class HourlyForecastView(context: Context, attrs: AttributeSet) :
    HorizontalScrollView(context, attrs) {

    private val aggregateView = AggregateView(context)

    init {

        addView(aggregateView)
    }

    fun setHours(hours: List<Hour>) {

        aggregateView.hours = hours
    }

    class Hour(
        val time: ZonedDateTime,
        val weatherCondition: WeatherCondition,
        val element: ForecastElement
    )

    class AggregateView(context: Context) : ViewGroup(context) {

        var hours = listOf<Hour>()
            set(value) {

                field = value
                removeViews(1, childCount - 1)

                hourViews = Array(value.size) {

                    val hourView = HourView(context)
                    hourView.setHour(value[it])
                    addView(hourView)
                    hourView
                }
            }

        private val graphView = GraphView(context)
        private var hourViews = arrayOf<HourView>()

        private var maxHourWidth = 0
        private var maxHourHeight = 0
        private val graphHeight = resources.getDimensionPixelSize(R.dimen.hourly_view_graph_height)
        private val graphMargin = resources.getDimensionPixelSize(R.dimen.hourly_view_graph_margin)
        private val hourSpacing = resources.getDimensionPixelSize(R.dimen.hourly_view_hour_spacing)

        init {

            addView(graphView)
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

            maxHourWidth = 0
            maxHourHeight = 0
            hourViews.forEach {

                it.measure(widthMeasureSpec, heightMeasureSpec)
                maxHourWidth = max(it.measuredWidth, maxHourWidth)
                maxHourHeight = max(it.measuredHeight, maxHourHeight)
            }

            initialiseGraph()

            val totalWidth =
                hourViews.size * maxHourWidth + (hourViews.size - 1) * hourSpacing
            setMeasuredDimension(totalWidth, graphHeight + graphMargin + maxHourHeight)
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

            initialiseGraph()

            super.onSizeChanged(w, h, oldw, oldh)
        }

        private fun initialiseGraph() {

            if (hours.isNotEmpty()) {

                val featureDoubles = hours.map { it.element.doubleValue }
                val maxFeatureDouble = featureDoubles.maxOrNull()!!
                val minFeatureDouble = featureDoubles.minOrNull()!!

                graphView.points = Array(hours.size) {

                    val point = PointF()

                    val x = (maxHourWidth + hourSpacing) * it + maxHourWidth.toFloat() / 2
                    val y =
                        graphHeight - ((featureDoubles[it] - minFeatureDouble) / (maxFeatureDouble - minFeatureDouble)) * graphHeight

                    point.set(x, y.toFloat())
                    point
                }
            }
        }

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

            graphView.layout(0, 0, measuredWidth, graphHeight)

            var xOffset = 0
            val yOffset = graphHeight + graphMargin
            hourViews.forEach {

                it.layout(xOffset, yOffset, xOffset + maxHourWidth, yOffset + maxHourHeight)
                xOffset += maxHourWidth + hourSpacing
            }
        }
    }

    class GraphView(context: Context) : View(context) {

        var points = arrayOf<PointF>()
            set(value) {

                field = value
                computeAdjustedPoints()
                computeControlPoints()
                invalidate()
            }

        // TODO These can be made more efficient by using a data structure that doesn't destroy the objects
        // TODO Maybe only invalidate these in onDraw, size changed being called in parent invalidates them, then they do it themselves - overkill?
        private val adjustedPoints = arrayListOf<PointF>()
        private val controlPoints = arrayListOf<ControlPoints>()

        private val linePaint = Paint().apply {

            isAntiAlias = true
            strokeWidth = resources.getDimension(R.dimen.hourly_view_graph_stroke)
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }

        private val linePath = Path()

        private fun computeAdjustedPoints() {

            adjustedPoints.clear()
            adjustedPoints.ensureCapacity(points.size)

            for (i in points.indices) {

                val point = points[i]

                val h = height.toFloat()
                val w = width.toFloat()
                val sw = linePaint.strokeWidth

                val adjustedX = point.x * ((w - sw) / w) + (sw / 2)
                val adjustedY = point.y * ((h - sw) / h) + (sw / 2)

                adjustedPoints.add(PointF(adjustedX, adjustedY))
            }
        }

        private fun computeControlPoints() {

            controlPoints.clear()
            controlPoints.ensureCapacity(adjustedPoints.size - 1)

            for (i in 0 until adjustedPoints.size - 1) {

                val p1 = adjustedPoints[i]
                val p2 = adjustedPoints[i + 1]

                val midX = (p1.x + p2.x) / 2

                controlPoints.add(ControlPoints().apply {

                    start.set(midX, p1.y)
                    end.set(midX, p2.y)
                })
            }
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

            computeAdjustedPoints()
            computeControlPoints()
            super.onSizeChanged(w, h, oldw, oldh)
        }

        override fun onDraw(canvas: Canvas?) {

            if (canvas != null && points.size > 1) {

                linePath.rewind()

                val firstPoint = adjustedPoints.first()
                linePath.moveTo(
                    firstPoint.x + linePaint.strokeWidth,
                    firstPoint.y
                )

                for (i in 0 until points.size - 1) {

                    val nextPoint = adjustedPoints[i + 1]
                    val controlPoint = controlPoints[i]

                    val x = nextPoint.x - if (i == points.size - 2) linePaint.strokeWidth else 0F
                    linePath.cubicTo(
                        controlPoint.start.x,
                        controlPoint.start.y,
                        controlPoint.end.x,
                        controlPoint.end.y,
                        x,
                        nextPoint.y
                    )
                }

                canvas.drawPath(linePath, linePaint)
            }
        }

        class ControlPoints {

            val start = PointF()
            val end = PointF()
        }
    }

    class HourView(context: Context) : LinearLayout(context) {

        init {

            orientation = VERTICAL
            inflate(context, R.layout.hourly_forecast_view_hour, this)
        }

        fun setHour(hour: Hour) {

            hourly_forecast_hour_content.text =
                ForecastDisplayUtil.getElementDisplayString(context, hour.element)

            WeatherConditionDisplay.getImageResource(hour.weatherCondition, false)?.also {

                hourly_forecast_hour_icon.setImageResource(it)
            }

            hourly_forecast_hour_time.text = TimeDisplayUtil.displayTime(hour.time)

        }
    }
}