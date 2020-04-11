package com.example.mycanvasview.widgets

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.mycanvasview.R
import kotlin.math.abs

class MyCanvasView : View {
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    private var STROKE_WIDTH = 12f
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeWidth = STROKE_WIDTH
        color = drawColor
    }
    private var historyPath = Path()
    private var curPath = Path()
    private var motionEventX  = 0f
    private var motionEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame : Rect

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val insect = 50
        // By definition, memory leak means we are holding a reference to an unused object in the heap.
        // In our case, the extraBitmap swap its reference to the brand-new bitmap
        frame = Rect(insect, insect, w - insect, h - insect)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(backgroundColor)
        canvas.drawRect(frame, paint)
        canvas.clipRect(frame)
        canvas.drawPath(historyPath, paint)
        canvas.drawPath(curPath, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionEventX = event.x
        motionEventY = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart(){
        curPath.reset()
        curPath.moveTo(motionEventX, motionEventY)
        updateCurrentPosition()

    }

    private fun touchMove(){
        val dx = abs(motionEventX - currentX)
        val dy = abs(motionEventY - currentY)
        if(dx >= touchTolerance || dy >= touchTolerance){
            curPath.quadTo(currentX, currentY, (currentX + motionEventX) / 2f, (currentY + motionEventY) / 2f)
            updateCurrentPosition()
        }
        invalidate()
    }

    /**
     *
     */
    private fun updateCurrentPosition(){
        currentX = motionEventX
        currentY = motionEventY
    }

    private fun touchUp(){
        historyPath.addPath(curPath)
        curPath.reset()
    }

}

