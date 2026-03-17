package com.example.flipclock

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator

class FlipDigit @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentDigit = 0
    private var nextDigit = 0
    private var flipProgress = 0f
    private var isFlipping = false

    // 3D变换相机
    private val camera = Camera()
    private val matrix = Matrix()

    // 数字画笔
    private val textPaint = Paint().apply {
        color = Color.parseColor("#FAFAFA")
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        isFakeBoldText = true
    }

    // 上半卡片背景
    private val topCardPaint = Paint().apply {
        isAntiAlias = true
    }

    // 下半卡片背景
    private val bottomCardPaint = Paint().apply {
        isAntiAlias = true
    }

    // 卡片外部阴影
    private val outerShadowPaint = Paint().apply {
        isAntiAlias = true
    }

    // 中缝凹陷阴影
    private val gapShadowPaint = Paint().apply {
        isAntiAlias = true
    }

    // 中缝高光
    private val gapHighlightPaint = Paint().apply {
        isAntiAlias = true
    }

    // 翻页动态阴影
    private val flipShadowPaint = Paint().apply {
        isAntiAlias = true
    }

    // 边缘高光
    private val edgeHighlightPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val viewHeight = h.toFloat()

        // 字体大小
        textPaint.textSize = viewHeight * 0.55f
        textPaint.setShadowLayer(viewHeight * 0.015f, 0f, viewHeight * 0.01f, Color.parseColor("#25000000"))

        // 上半卡片渐变 - 模拟光照，底部颜色与下半卡片顶部一致
        topCardPaint.shader = LinearGradient(
            0f, 0f, 0f, viewHeight / 2,
            Color.parseColor("#484848"),  // 顶部较亮
            Color.parseColor("#383838"),  // 底部与下半卡片顶部相同
            Shader.TileMode.CLAMP
        )

        // 下半卡片渐变 - 顶部颜色与上半卡片底部一致
        bottomCardPaint.shader = LinearGradient(
            0f, viewHeight / 2, 0f, viewHeight,
            Color.parseColor("#383838"),  // 顶部与上半卡片底部相同
            Color.parseColor("#2C2C2C"),  // 底部更暗
            Shader.TileMode.CLAMP
        )

        // 外部阴影
        outerShadowPaint.shader = LinearGradient(
            0f, 0f, 0f, viewHeight,
            Color.parseColor("#00000000"),
            Color.parseColor("#40000000"),
            Shader.TileMode.CLAMP
        )

        // 中缝阴影 - 凹陷效果
        gapShadowPaint.shader = LinearGradient(
            0f, viewHeight / 2 - viewHeight * 0.03f,
            0f, viewHeight / 2 + viewHeight * 0.03f,
            Color.parseColor("#00000000"),
            Color.parseColor("#FF000000"),  // 深黑色凹陷
            Shader.TileMode.CLAMP
        )

        // 中缝高光
        gapHighlightPaint.color = Color.parseColor("#30FFFFFF")

        // 边缘高光
        edgeHighlightPaint.strokeWidth = viewHeight * 0.005f
        edgeHighlightPaint.color = Color.parseColor("#12FFFFFF")
    }

    fun setDigit(digit: Int) {
        val validDigit = digit.coerceIn(0, 9)
        if (validDigit == currentDigit || isFlipping) return

        nextDigit = validDigit
        startFlipAnimation()
    }

    private fun startFlipAnimation() {
        isFlipping = true
        flipProgress = 0f

        // 单一动画，模拟真实翻页
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 800
            interpolator = OvershootInterpolator(0.8f)  // 带轻微回弹
            addUpdateListener { animator ->
                flipProgress = animator.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    currentDigit = nextDigit
                    isFlipping = false
                    flipProgress = 0f
                    invalidate()
                }
            })
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val midY = h / 2
        val radius = h * 0.08f
        val gapHeight = h * 0.015f  // 中缝高度
        val shadowOffset = h * 0.04f  // 外部阴影偏移

        // 绘制外部阴影
        canvas.drawRoundRect(RectF(-shadowOffset, -shadowOffset, w + shadowOffset, h + shadowOffset), radius + 4f, radius + 4f, outerShadowPaint)

        if (!isFlipping) {
            drawStaticState(canvas, w, h, midY, radius, gapHeight)
        } else {
            drawFlippingState(canvas, w, h, midY, radius, gapHeight)
        }
    }

    private fun drawStaticState(
        canvas: Canvas, w: Float, h: Float, midY: Float, radius: Float, gapHeight: Float
    ) {
        // 1. 绘制上半卡片
        canvas.save()
        canvas.clipRect(0f, 0f, w, midY - gapHeight / 2)
        drawTopCard(canvas, w, h, radius, currentDigit.toString())
        canvas.restore()

        // 2. 绘制下半卡片
        canvas.save()
        canvas.clipRect(0f, midY + gapHeight / 2, w, h)
        drawBottomCard(canvas, w, h, radius, currentDigit.toString())
        canvas.restore()

        // 3. 绘制中缝凹陷效果
        drawGapEffect(canvas, w, midY, h)

        // 4. 绘制边缘高光
        drawEdgeHighlight(canvas, w, h, radius)
    }

    private fun drawFlippingState(
        canvas: Canvas, w: Float, h: Float, midY: Float, radius: Float, gapHeight: Float
    ) {
        val progress = flipProgress

        // 翻页分为两个阶段：
        // 阶段1 (0-0.5): 上半卡片向下翻转90度
        // 阶段2 (0.5-1): 下半卡片从上方翻入

        if (progress < 0.5f) {
            // === 阶段1：上半卡片翻转 ===
            val flipAngle = progress * 2 * 90f  // 0 -> 90度

            // 1. 绘制下半卡片（保持不变，显示当前数字）
            canvas.save()
            canvas.clipRect(0f, midY + gapHeight / 2, w, h)
            drawBottomCard(canvas, w, h, radius, currentDigit.toString())
            canvas.restore()

            // 2. 绘制翻转中的上半卡片
            canvas.save()
            canvas.clipRect(0f, 0f, w, midY)

            // 应用3D翻转
            canvas.translate(0f, midY)
            camera.save()
            camera.rotateX(-flipAngle)
            camera.getMatrix(matrix)
            canvas.concat(matrix)
            camera.restore()
            canvas.translate(0f, -midY)

            // 绘制上半卡片（显示当前数字）
            drawTopCard(canvas, w, h, radius, currentDigit.toString())

            // 绘制翻转阴影
            val shadowIntensity = (progress * 2 * 180).toInt()
            flipShadowPaint.color = Color.argb(shadowIntensity, 0, 0, 0)
            canvas.drawRect(0f, 0f, w, midY, flipShadowPaint)

            canvas.restore()

            // 3. 绘制中缝
            drawGapEffect(canvas, w, midY, h)

        } else {
            // === 阶段2：下半卡片翻入 ===
            val flipAngle = (1 - (progress - 0.5f) * 2) * 90f  // 90 -> 0度

            // 1. 绘制上半卡片（显示新数字）
            canvas.save()
            canvas.clipRect(0f, 0f, w, midY - gapHeight / 2)
            drawTopCard(canvas, w, h, radius, nextDigit.toString())
            canvas.restore()

            // 2. 先绘制静态下半卡片作为背景（避免黑色闪烁）
            canvas.save()
            canvas.clipRect(0f, midY + gapHeight / 2, w, h)
            drawBottomCard(canvas, w, h, radius, nextDigit.toString())
            canvas.restore()

            // 3. 绘制翻入中的下半卡片（覆盖在静态背景上）
            canvas.save()
            canvas.clipRect(0f, midY, w, h)

            // 应用3D翻转
            canvas.translate(0f, midY)
            camera.save()
            camera.rotateX(flipAngle)
            camera.getMatrix(matrix)
            canvas.concat(matrix)
            camera.restore()
            canvas.translate(0f, -midY)

            // 绘制下半卡片（显示新数字）
            drawBottomCard(canvas, w, h, radius, nextDigit.toString())

            canvas.restore()

            // 4. 绘制中缝
            drawGapEffect(canvas, w, midY, h)
        }

        // 绘制边缘高光
        drawEdgeHighlight(canvas, w, h, radius)
    }

    private fun drawTopCard(canvas: Canvas, w: Float, h: Float, radius: Float, digit: String) {
        val midY = h / 2
        val textY = midY + textPaint.textSize * 0.32f

        // 背景
        canvas.drawRoundRect(RectF(0f, 0f, w, h), radius, radius, topCardPaint)

        // 数字
        canvas.drawText(digit, w / 2, textY, textPaint)
    }

    private fun drawBottomCard(canvas: Canvas, w: Float, h: Float, radius: Float, digit: String) {
        val midY = h / 2
        val textY = midY + textPaint.textSize * 0.32f

        // 背景
        canvas.drawRoundRect(RectF(0f, 0f, w, h), radius, radius, bottomCardPaint)

        // 数字
        canvas.drawText(digit, w / 2, textY, textPaint)
    }

    private fun drawGapEffect(canvas: Canvas, w: Float, midY: Float, h: Float) {
        // 中缝中心线（柔和的阴影线）- 使用相对比例
        gapHighlightPaint.strokeWidth = h * 0.006f
        gapHighlightPaint.color = Color.parseColor("#282828")
        canvas.drawLine(0f, midY, w, midY, gapHighlightPaint)

        // 上边缘微弱高光
        gapHighlightPaint.strokeWidth = h * 0.003f
        gapHighlightPaint.color = Color.parseColor("#18FFFFFF")
        canvas.drawLine(0f, midY - h * 0.003f, w, midY - h * 0.003f, gapHighlightPaint)
    }

    private fun drawEdgeHighlight(canvas: Canvas, w: Float, h: Float, radius: Float) {
        // 左边缘高光 - 使用相对比例
        canvas.drawLine(h * 0.005f, radius, h * 0.005f, h - radius, edgeHighlightPaint)

        // 右边缘高光
        canvas.drawLine(w - h * 0.005f, radius, w - h * 0.005f, h - radius, edgeHighlightPaint)

        // 顶部边缘高光
        canvas.drawLine(radius, h * 0.005f, w - radius, h * 0.005f, edgeHighlightPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setLayerType(LAYER_TYPE_NONE, null)
    }
}
