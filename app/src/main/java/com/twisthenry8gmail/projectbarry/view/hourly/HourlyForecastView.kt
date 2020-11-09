package com.twisthenry8gmail.projectbarry.view.hourly

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class HourlyForecastView(context: Context, attrs: AttributeSet) :
    HorizontalScrollView(context, attrs) {

//    private val aggregateView = AggregateView(context)
//
//    init {
//
//        addView(aggregateView)
//    }
//
//    fun setForecast(forecast: HourlyForecast2) {
//
//        aggregateView.forecast = forecast
//    }
//
//    class AggregateView(context: Context) : ViewGroup(context) {
//
//        var forecast: HourlyForecast2? = null
//            set(value) {
//
//                field = value
//                removeViews(1, childCount - 1)
//
//                value?.also { forecastValue ->
//
//                    hourViews = Array(forecastValue.hours.size) {
//
//                        val hourView = HourView(context)
//                        hourView.setHour(forecastValue.hours[it])
//                        addView(hourView)
//                        hourView
//                    }
//                }
//            }
//
//        private val graphView = GraphView(context)
//        private var hourViews = arrayOf<HourView>()
//
//        private var maxHourWidth = 0
//        private var maxHourHeight = 0
//        private val graphHeight = resources.getDimensionPixelSize(R.dimen.hourly_view_graph_height)
//        private val graphMargin = resources.getDimensionPixelSize(R.dimen.hourly_view_graph_margin)
//        private val hourSpacing = resources.getDimensionPixelSize(R.dimen.hourly_view_hour_spacing)
//
//        init {
//
//            addView(graphView)
//        }
//
//        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//
//            maxHourWidth = 0
//            maxHourHeight = 0
//            hourViews.forEach {
//
//                it.measure(widthMeasureSpec, heightMeasureSpec)
//                maxHourWidth = max(it.measuredWidth, maxHourWidth)
//                maxHourHeight = max(it.measuredHeight, maxHourHeight)
//            }
//
//            initialiseGraph()
//
//            val totalWidth =
//                hourViews.size * maxHourWidth + (hourViews.size - 1) * hourSpacing
//            setMeasuredDimension(totalWidth, graphHeight + graphMargin + maxHourHeight)
//        }
//
//        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//
//            initialiseGraph()
//
//            super.onSizeChanged(w, h, oldw, oldh)
//        }
//
//        private fun initialiseGraph() {
//
//            forecast?.also { forecast ->
//
//                if (forecast.hours.isNotEmpty()) {
//
//                    val values = forecast.hours.map { it.value }
//                    val minValue = forecast.minValue
//                    val maxValue = forecast.maxValue
//
//                    graphView.points = Array(forecast.hours.size) {
//
//                        val point = PointF()
//
//                        val x = (maxHourWidth + hourSpacing) * it + maxHourWidth.toFloat() / 2
//                        val y =
//                            graphHeight - ((values[it] - minValue) / (maxValue - minValue)) * graphHeight
//
//                        point.set(x, y.toFloat())
//                        point
//                    }
//                }
//            }
//        }
//
//        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//
//            graphView.layout(0, 0, measuredWidth, graphHeight)
//
//            var xOffset = 0
//            val yOffset = graphHeight + graphMargin
//            hourViews.forEach {
//
//                it.layout(xOffset, yOffset, xOffset + maxHourWidth, yOffset + maxHourHeight)
//                xOffset += maxHourWidth + hourSpacing
//            }
//        }
//    }
//
//    class GraphView(context: Context) : View(context) {
//
//        var points = arrayOf<PointF>()
//            set(value) {
//
//                field = value
//                computeAdjustedPoints()
//                computeControlPoints()
//                invalidate()
//            }
//
//        // TODO These can be made more efficient by using a data structure that doesn't destroy the objects
//        // TODO Maybe only invalidate these in onDraw, size changed being called in parent invalidates them, then they do it themselves - overkill?
//        private val adjustedPoints = arrayListOf<PointF>()
//        private val controlPoints = arrayListOf<ControlPoints>()
//
//        private val linePaint = Paint().apply {
//
//            isAntiAlias = true
//            strokeWidth = resources.getDimension(R.dimen.hourly_view_graph_stroke)
//            strokeCap = Paint.Cap.ROUND
//            style = Paint.Style.STROKE
//        }
//
//        private val linePath = Path()
//
//        private fun computeAdjustedPoints() {
//
//            adjustedPoints.clear()
//            adjustedPoints.ensureCapacity(points.size)
//
//            for (i in points.indices) {
//
//                val point = points[i]
//
//                val h = height.toFloat()
//                val w = width.toFloat()
//                val sw = linePaint.strokeWidth
//
//                val adjustedX = point.x * ((w - sw) / w) + (sw / 2)
//                val adjustedY = point.y * ((h - sw) / h) + (sw / 2)
//
//                adjustedPoints.add(PointF(adjustedX, adjustedY))
//            }
//        }
//
//        private fun computeControlPoints() {
//
//            controlPoints.clear()
//            controlPoints.ensureCapacity(adjustedPoints.size - 1)
//
//            for (i in 0 until adjustedPoints.size - 1) {
//
//                val p1 = adjustedPoints[i]
//                val p2 = adjustedPoints[i + 1]
//
//                val midX = (p1.x + p2.x) / 2
//
//                controlPoints.add(ControlPoints().apply {
//
//                    start.set(midX, p1.y)
//                    end.set(midX, p2.y)
//                })
//            }
//        }
//
//        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//
//            computeAdjustedPoints()
//            computeControlPoints()
//            super.onSizeChanged(w, h, oldw, oldh)
//        }
//
//        override fun onDraw(canvas: Canvas?) {
//
//            if (canvas != null && points.size > 1) {
//
//                linePath.rewind()
//
//                val firstPoint = adjustedPoints.first()
//                linePath.moveTo(linePaint.strokeWidth, firstPoint.y)
//                linePath.lineTo(firstPoint.x, firstPoint.y)
//
//                for (i in 0 until points.size - 1) {
//
//                    val nextPoint = adjustedPoints[i + 1]
//                    val controlPoint = controlPoints[i]
//
//                    val x = nextPoint.x
//                    linePath.cubicTo(
//                        controlPoint.start.x,
//                        controlPoint.start.y,
//                        controlPoint.end.x,
//                        controlPoint.end.y,
//                        x,
//                        nextPoint.y
//                    )
//                }
//
//                val lastPoint = adjustedPoints.last()
//                linePath.lineTo(width - linePaint.strokeWidth, lastPoint.y)
//
//                canvas.drawPath(linePath, linePaint)
//            }
//        }
//
//        class ControlPoints {
//
//            val start = PointF()
//            val end = PointF()
//        }
//    }
//
//    class HourView(context: Context) : LinearLayout(context) {
//
//        init {
//
//            orientation = VERTICAL
//            inflate(context, R.layout.hourly_forecast_view_hour, this)
//        }
//
//        fun setHour(hour: HourlyForecast2.Hour) {
//
//            hourly_forecast_hour_content.text =
//                ForecastDisplayUtil.getElementDisplayString(context, hour.element)
//
//            WeatherConditionDisplay.getImageResource(hour.weatherCondition)?.also {
//
//                hourly_forecast_hour_icon.setImageResource(it)
//            }
//
//            hourly_forecast_hour_time.text = TimeDisplayUtil.displayTime(hour.time)
//
//        }
//    }
}