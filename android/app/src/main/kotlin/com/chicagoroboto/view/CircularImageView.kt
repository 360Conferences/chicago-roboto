package com.chicagoroboto.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import com.chicagoroboto.R

class CircularImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

  private val clipPath = Path()
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
  }

  init {
    scaleType = ScaleType.CENTER_CROP

    val a = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyleAttr, 0)
    try {
      val defaultBorderWidth = DEFAULT_BORDER_WIDTH * resources.displayMetrics.density;
      strokePaint.strokeWidth = a.getDimension(R.styleable.CircularImageView_borderWidth, defaultBorderWidth)
      strokePaint.color = a.getColor(R.styleable.CircularImageView_borderColor, DEFAULT_BORDER_COLOR)
    } finally {
      a.recycle()
    }
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)

    val strokeWidth = strokePaint.strokeWidth
    val w = right - left - paddingStart - paddingEnd - strokeWidth * 2.0f
    val h = bottom - top - paddingTop - paddingBottom - strokeWidth * 2.0f
    clipPath.reset()
    clipPath.addCircle(
        w / 2.0f + strokeWidth,
        h / 2.0f + strokeWidth,
        minOf(w, h) / 2.0f,
        Path.Direction.CW
    )
  }

  override fun onDraw(canvas: Canvas?) {
    canvas?.withSave {
      clipPath(clipPath)
      super.onDraw(canvas)
    }
    canvas?.drawPath(clipPath, strokePaint)
  }

  companion object {
    private const val DEFAULT_BORDER_WIDTH = 0
    private const val DEFAULT_BORDER_COLOR = 0
  }
}
