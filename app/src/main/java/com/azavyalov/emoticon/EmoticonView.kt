package com.azavyalov.emoticon

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.azavyalov.emoticon.PaintType.*
import kotlin.math.min

class EmoticonView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var faceBackgroundColor = DEFAULT_MAIN_COLOR
    private var eyesColor = DEFAULT_EYES_COLOR
    private var mouthColor = DEFAULT_MOUTH_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    private val mouthPath = Path()
    private var size = 0

    var faceState = HAPPY
        set(value) {
            field = value
            // This method calls onDraw() to redraw after state was changed
            invalidate()
        }

    init {
        setupAttributes(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        size = min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putLong(FACE_STATE_KEY, faceState)
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            faceState = newState.getLong(FACE_STATE_KEY, HAPPY)
            newState = newState.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(newState)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(
            attrs, R.styleable.EmoticonView,
            0, 0
        )
        // Extract attributes and assign them to variables
        faceState = ta.getColor(R.styleable.EmoticonView_state, HAPPY.toInt()).toLong()
        faceBackgroundColor = ta.getColor(R.styleable.EmoticonView_mainColor, DEFAULT_MAIN_COLOR)
        eyesColor = ta.getColor(R.styleable.EmoticonView_eyesColor, DEFAULT_EYES_COLOR)
        mouthColor = ta.getColor(R.styleable.EmoticonView_mouthColor, DEFAULT_MOUTH_COLOR)
        borderColor = ta.getColor(R.styleable.EmoticonView_borderColor, DEFAULT_BORDER_COLOR)
        borderWidth = ta.getDimension(R.styleable.EmoticonView_borderWidth, DEFAULT_BORDER_WIDTH)
        // Array should be recycled after use
        ta.recycle()
    }

    private fun drawBackground(canvas: Canvas) {
        val radius = size / 2f
        canvas.drawCircle(size / 2f, size / 2f, radius, getCustomPaint(BACKGROUND))
        canvas.drawCircle(
            size / 2f, size / 2f, radius - borderWidth / 2f,
            getCustomPaint(BORDER)
        )
    }

    private fun drawEyes(canvas: Canvas) {
        val paint = getCustomPaint(EYES)
        when (faceState) {
            HAPPY -> drawHappyEyes(canvas, paint)
            SAD -> drawSadEyes(canvas, paint)
            else -> {
                throw IllegalArgumentException("Unknown state")
            }
        }
    }

    private fun drawHappyEyes(canvas: Canvas, paint: Paint) {
        val leftEye = RectF(size * 0.32f, size * 0.23f, size * 0.43f, size * 0.50f)
        canvas.drawOval(leftEye, paint)
        val rightEye = RectF(size * 0.57f, size * 0.23f, size * 0.68f, size * 0.50f)
        canvas.drawOval(rightEye, paint)
    }

    private fun drawSadEyes(canvas: Canvas, paint: Paint) {
        val leftEye = RectF(size * 0.29f, size * 0.27f, size * 0.46f, size * 0.46f)
        canvas.drawOval(leftEye, paint)
        val rightEye = RectF(size * 0.55f, size * 0.27f, size * 0.72f, size * 0.46f)
        canvas.drawOval(rightEye, paint)
    }

    private fun drawMouth(canvas: Canvas) {
        mouthPath.reset()

        mouthPath.moveTo(size * 0.22f, size * 0.7f)
        if (faceState == HAPPY) {
            mouthPath.quadTo(size * 0.5f, size * 0.80f, size * 0.78f, size * 0.7f)
            mouthPath.quadTo(size * 0.5f, size * 0.90f, size * 0.22f, size * 0.7f)
        } else {
            mouthPath.quadTo(size * 0.5f, size * 0.50f, size * 0.78f, size * 0.7f)
            mouthPath.quadTo(size * 0.5f, size * 0.60f, size * 0.22f, size * 0.7f)
        }

        canvas.drawPath(mouthPath, getCustomPaint(MOUTH))
    }

    private fun getCustomPaint(type: PaintType): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        return when (type) {
            BACKGROUND -> paint.apply {
                color = faceBackgroundColor
                style = FILL
            }
            BORDER -> paint.apply {
                color = borderColor
                style = STROKE
                strokeWidth = borderWidth
            }
            EYES -> paint.apply {
                color = eyesColor
                style = FILL
            }
            MOUTH -> paint.apply {
                color = mouthColor
                style = FILL
            }
        }
    }

    companion object {
        private const val DEFAULT_MAIN_COLOR = Color.YELLOW
        private const val DEFAULT_EYES_COLOR = Color.BLACK
        private const val DEFAULT_MOUTH_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH = 4.0f

        private const val FACE_STATE_KEY = "STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"

        const val HAPPY = 0L
        const val SAD = 1L
    }
}