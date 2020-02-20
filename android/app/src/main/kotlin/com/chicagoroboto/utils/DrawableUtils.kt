package com.chicagoroboto.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.core.content.ContextCompat

object DrawableUtils {

  /**
   * Creates a [Drawable] by either creating it from a vector drawable resource or falling back
   * to a plain old drawable resource.
   */
  @CheckResult fun create(context: Context, @DrawableRes resourceId: Int): Drawable? {
    return try {
      VectorDrawableCompat.create(context.resources, resourceId, context.theme)
    } catch (exception: Resources.NotFoundException) {
      ContextCompat.getDrawable(context, resourceId)
    }
  }

}

/**
 * Draws the Drawable to a bitmap.
 */
fun Drawable.asBitmap(width: Int = intrinsicWidth, height: Int = intrinsicHeight): Bitmap {
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  draw(canvas)
  return bitmap

}
