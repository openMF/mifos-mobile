package org.mifos.mobile.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import org.mifos.mobile.R

class CircularImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ImageView(context, attrs) {
    private var borderWidth = 0
    private var canvasSize = 0
    private var image: Bitmap? = null
    private val paint: Paint
    private val paintBorder: Paint?
    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        requestLayout()
        this.invalidate()
    }

    fun setBorderColor(borderColor: Int) {
        if (paintBorder != null) paintBorder.color = borderColor
        this.invalidate()
    }

    fun addShadow() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, paintBorder)
        paintBorder?.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK)
    }

    public override fun onDraw(canvas: Canvas) {
        // load the bitmap
        image = drawableToBitmap(drawable)

        // init shader
        if (image != null) {
            canvasSize = canvas.width
            if (canvas.height < canvasSize) canvasSize = canvas.height
            val shader = BitmapShader(Bitmap.createScaledBitmap(image, canvasSize,
                    canvasSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader

            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shader that will texture the shape
            val circleCenter = (canvasSize - borderWidth * 2) / 2
            canvas.drawCircle(circleCenter + borderWidth.toFloat(), circleCenter + borderWidth.toFloat(), (canvasSize - borderWidth * 2) / 2 + borderWidth - 4.0f, paintBorder)
            canvas.drawCircle(circleCenter + borderWidth.toFloat(), circleCenter + borderWidth.toFloat(), (canvasSize - borderWidth * 2) / 2 - 4.0f, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            specSize
        } else {
            // The parent has not imposed any constraint on the child.
            canvasSize
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            specSize
        } else {
            // Measure the text (beware: ascent is a negative number)
            canvasSize
        }
        return result + 2
    }

    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        } else if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable
                .intrinsicHeight, Bitmap.Config.ARGB_8888)
        val bitmapScaled = Bitmap.createScaledBitmap(bitmap, bitmap.height / 5, bitmap
                .height / 5, true)
        val canvas = Canvas(bitmapScaled)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmapScaled
    }

    init {

        // init paint
        paint = Paint()
        paint.isAntiAlias = true
        paintBorder = Paint()
        paintBorder.setAntiAlias(true)

        // load the styled attributes and set their properties
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView)
        if (attributes.getBoolean(R.styleable.CircularImageView_border, true)) {
            val defaultBorderSize = (4 * getContext().resources.displayMetrics.density + 0.5f).toInt()
            setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircularImageView_border_width, defaultBorderSize))
            setBorderColor(attributes.getColor(R.styleable.CircularImageView_border_color, Color.WHITE))
        }
        if (attributes.getBoolean(R.styleable.CircularImageView_shadow, false)) addShadow()
    }
}