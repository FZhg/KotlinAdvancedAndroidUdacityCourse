package com.example.clippingexample.widgets

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.clippingexample.R

class ClippingView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    private val path = Path()
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)
    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)
    private val circleRadius = resources.getDimension(R.dimen.circleRadius)
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight
    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f * clipRectBottom)
    private var rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )
    private val rejectRow = rowFour + rectInset + 2 * clipRectBottom

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        drawBackAndUnclippedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawSkewedTextExample(canvas)
        drawTranslatedTextExample(canvas)
        drawQuickRejectExample(canvas)
    }

    private fun drawClippedRectangle(canvas: Canvas){
        // Limit the canvas to a square of 90x90 or 120x120 depending on the smallest screen width
        canvas.clipRect(
            clipRectLeft,
            clipRectTop,
            clipRectRight,
            clipRectBottom
        )

        // Draw the white background in the square
        canvas.drawColor(Color.WHITE)

        // Draw the diagonal
        paint.color = Color.RED
        canvas.drawLine(
            clipRectLeft,
            clipRectTop,
            clipRectRight,
            clipRectRight,
            paint
        )

        // Draw the Green circle
        paint.color = Color.GREEN
        canvas.drawCircle(
            circleRadius,
            clipRectBottom - circleRadius,
            circleRadius,
            paint
        )

        paint.color = Color.BLUE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            context.getString(R.string.clipping),
            clipRectRight,
            textOffset,
            paint
        )
    }


    private fun drawBackAndUnclippedRectangle(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        canvas.save()
        canvas.translate(columnOne, rowOne)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawDifferenceClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo, rowOne)
        canvas.clipRect(
            2  * rectInset,
            2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        canvas.clipOutRect(
            4 * rectInset,
            4 * rectInset,
            clipRectRight - 4 * rectInset,
            clipRectBottom - 4 * rectInset
        )
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawCircularClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowTwo)
        path.rewind()
        path.addCircle(
            circleRadius,
            clipRectBottom - circleRadius,
            circleRadius,
            Path.Direction.CCW
        )
        canvas.clipOutPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawIntersectionClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo,rowTwo)
        canvas.clipRect(
            clipRectLeft,
            clipRectTop,
            clipRectRight - smallRectOffset,
            clipRectBottom - smallRectOffset
        )
        canvas.clipRect(
            clipRectLeft + smallRectOffset,
            clipRectTop + smallRectOffset,
            clipRectRight,
            clipRectBottom
        )
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawCombinedClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowThree)
        path.rewind()
        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + rectInset + circleRadius,
            circleRadius,
            Path.Direction.CCW
        )
        path.addRect(
            clipRectRight / 2 - circleRadius,
            clipRectTop + circleRadius + rectInset,
            clipRectRight / 2 + circleRadius,
            clipRectBottom - rectInset,
            Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawRoundedRectangleClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo, rowThree)
        path.rewind()
        path.addRoundRect(
            rectF,
            clipRectRight / 4,
            clipRectRight / 4,
            Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()

    }

    private fun drawOutsideClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowFour)
        canvas.clipRect(
            2 * rectInset,
            2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawTranslatedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = Color.GREEN
        paint.textAlign = Paint.Align.LEFT
        canvas.translate(columnTwo, textRow)
        canvas.drawText(context.getString(R.string.translated), clipRectLeft, clipRectTop, paint)
        canvas.restore()
    }

    private fun drawSkewedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.RIGHT
        canvas.translate(columnTwo, textRow)
        canvas.skew(0.2f , 0.3f)
        canvas.drawText(context.getString(R.string.skewed), clipRectLeft, clipRectTop, paint)
        canvas.restore()
    }

    private fun drawQuickRejectExample(canvas: Canvas) {
        val inClipRectangle = RectF(clipRectRight / 2,
            clipRectBottom / 2,
            clipRectRight * 2,
            clipRectBottom * 2)

        val notInClipRectangle = RectF(RectF(clipRectRight+1,
            clipRectBottom+1,
            clipRectRight * 2,
            clipRectBottom * 2))

        canvas.save()
        canvas.translate(columnOne, rejectRow)
        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom
        )
        if (canvas.quickReject(notInClipRectangle, Canvas.EdgeType.AA)) {
            canvas.drawColor(Color.WHITE)
        }
        else {
            canvas.drawColor(Color.BLACK)
            canvas.drawRect(inClipRectangle, paint)
        }
        canvas.restore()
    }
}
