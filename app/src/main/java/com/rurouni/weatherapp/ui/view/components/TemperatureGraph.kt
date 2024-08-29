package com.rurouni.weatherapp.ui.view.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.rurouni.weatherapp.R
import com.rurouni.weatherapp.ui.model.TemperatureGraphData

class TemperatureGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paddingLeft: Float
    private val paddingRight: Float
    private val paddingTop: Float
    private val paddingBottom: Float
    private val strokeWidth : Float

    private val paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val circlePaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private lateinit var data : TemperatureGraphData

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TemperatureGraph,
            0, 0
        )

        try {
            val lineColor = a.getColor(R.styleable.TemperatureGraph_lineColor, Color.BLUE)
            val circleColor = a.getColor(R.styleable.TemperatureGraph_circleColor, Color.RED)
            paint.color = lineColor
            circlePaint.color = circleColor

            paddingLeft = a.getDimension(R.styleable.TemperatureGraph_startAndEndPadding, 20f)
            paddingRight = a.getDimension(R.styleable.TemperatureGraph_startAndEndPadding, 20f)
            paddingTop = a.getDimension(R.styleable.TemperatureGraph_topAndBottomPadding, 20f)
            paddingBottom = a.getDimension(R.styleable.TemperatureGraph_topAndBottomPadding, 20f)

            strokeWidth = a.getDimension(R.styleable.TemperatureGraph_strokeWidth, 5f)

            paint.strokeWidth = strokeWidth

        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLineChart(canvas)
    }

    private fun drawLineChart(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        val adjustedHeight = height - paddingTop - paddingBottom
        val startY = paddingTop

        val centerWidth = width / 2

        val startX = if (data.before == 0f) paddingLeft else 0f
        val endX = if (data.next == 0f) width - paddingRight else width

        val beforeHeight = startY + (adjustedHeight / (data.max - data.min)) * (data.max - data.before)
        val currentHeight = startY + (adjustedHeight / (data.max - data.min)) * (data.max - data.current)
        val nextHeight = startY + (adjustedHeight / (data.max - data.min)) * (data.max - data.next)

        if (data.current != 0f) {
            if (data.before == 0f) {
                drawLine(canvas, startX, currentHeight, centerWidth, currentHeight)
                drawLine(canvas, centerWidth, currentHeight, endX, ((nextHeight + currentHeight) / 2))
            } else if (data.next == 0f) {
                drawLine(canvas, startX, ((beforeHeight + currentHeight) / 2), centerWidth, currentHeight)
                drawLine(canvas, centerWidth, currentHeight, endX, currentHeight)
            } else {
                drawLine(canvas, startX, ((beforeHeight + currentHeight) / 2), centerWidth, currentHeight)
                drawLine(canvas, centerWidth, currentHeight, endX, ((nextHeight + currentHeight) / 2))
            }

            drawCircle(canvas, centerWidth, currentHeight)
        }
    }

    private fun drawLine(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {
        canvas.drawLine(startX, startY, endX, endY, paint)
    }

    private fun drawCircle(canvas: Canvas, x: Float, y: Float) {
        val radius = 10f
        canvas.drawCircle(x, y, radius, circlePaint)
    }

    fun setValues(data : TemperatureGraphData) {
        this.data = data
        invalidate()
    }

    fun animateLineColorChange(startColor: Int, endColor: Int, duration: Long) {
        val colorAnimator = ValueAnimator.ofArgb(startColor, endColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                paint.color = animator.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    fun animateCircleColorChange(startColor: Int, endColor: Int, duration: Long) {
        val colorAnimator = ValueAnimator.ofArgb(startColor, endColor).apply {
            this.duration = duration
            addUpdateListener { animator ->
                circlePaint.color = animator.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    fun setCircleColor(color: Int,) {
        circlePaint.color = color
        invalidate()
    }

}
