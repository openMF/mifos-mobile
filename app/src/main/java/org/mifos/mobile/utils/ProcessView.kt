package org.mifos.mobile.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import org.mifos.mobile.R

/**
 * Created by dilpreet on 30/6/17.
 */
class ProcessView : View {
    private var valueStr: String? = null
    private var textPaint: Paint? = null
    private var backgroundPaint: Paint? = null

    constructor(context: Context?) : super(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProcessView)
        valueStr = typedArray.getString(R.styleable.ProcessView_value)
        typedArray.recycle()
        textPaint = Paint()
        textPaint?.color = getColorCompat(android.R.color.white)
        textPaint?.isAntiAlias = true
        textPaint?.style = Paint.Style.FILL
        textPaint?.strokeWidth = 1f
        textPaint?.textSize = 40f
        backgroundPaint = Paint()
        backgroundPaint?.color = getColorCompat(R.color.gray_dark)
        backgroundPaint?.isAntiAlias = true
        backgroundPaint?.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val xPos = canvas.width / 2 - (textPaint?.measureText(valueStr.toString())?.div(2))?.toInt()!!
        val yPos = (canvas.height / 2 - ((textPaint?.descent()?.plus(textPaint?.ascent()!!))?.div(2))!!).toInt()
        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)
        val radius = usableWidth.coerceAtMost(usableHeight) / 2
        val cx = paddingLeft + usableWidth / 2
        val cy = paddingTop + usableHeight / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), backgroundPaint)
        canvas.drawText(valueStr, xPos.toFloat(), yPos.toFloat(), textPaint)
    }

    fun setCurrentActive() {
        backgroundPaint?.color = getColorCompat(R.color.primary)
        invalidate()
    }

    fun setCurrentCompleted() {
        backgroundPaint?.color = getColorCompat(R.color.primary)
        valueStr = "\u2713"
        invalidate()
    }

    private fun getColorCompat(colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }
}