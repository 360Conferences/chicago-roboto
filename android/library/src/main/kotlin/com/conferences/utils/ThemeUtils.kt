package com.conferences.utils

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v7.widget.TintTypedArray

fun Context.themeAttrColor(@AttrRes attr: Int): Int {
  val attrs = intArrayOf(attr)
  val a = TintTypedArray.obtainStyledAttributes(this, null, attrs)
  try {
    return a.getColor(0, 0)
  } finally {
    a.recycle()
  }
}
