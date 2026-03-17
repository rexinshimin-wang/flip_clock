package com.example.flipclock

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class BlinkingColon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var _alphaValue = 1f

    private val paint = Paint().apply {
        color = Color.parseColor("#E0E0E0") // 柔和白色
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        setShadowLayer(3f, 1f, 1f, Color.parseColor("#30000000"))
    }

    private val blinkAnimator: ObjectAnimator = ObjectAnimator.ofFloat(this, "alphaValue", 1f, 0.3f).apply {
        duration = 1000
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        interpolator = AccelerateDecelerateInterpolator()
    }

    // Property for animator
    var alphaValue: Float
        get() = _alphaValue
        set(value) {
            _alphaValue = value
            paint.alpha = (255 * value).toInt()
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paint.textSize = h * 0.5f
        paint.setShadowLayer(h * 0.02f, h * 0.01f, h * 0.01f, Color.parseColor("#30000000"))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        blinkAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        blinkAnimator.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        
        // Draw colon ":"
        canvas.drawText(":", centerX, centerY + paint.textSize * 0.35f, paint)
    }
}
