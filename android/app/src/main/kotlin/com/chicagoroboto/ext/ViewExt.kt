package com.chicagoroboto.ext

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

fun View.getActivity(): Activity? {
  var context = context
  while (context !is Activity) {
    if (context is ContextWrapper) {
      context = context.baseContext
    } else {
      context = null
    }
  }
  return context
}