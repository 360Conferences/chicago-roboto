package com.conferences.ext

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

fun View.getActivity(): Activity? {
  var context = context
  while (context !is Activity) {
    if (context is ContextWrapper) {
      context = context.baseContext
    } else {
      return null
    }
  }
  return context
}