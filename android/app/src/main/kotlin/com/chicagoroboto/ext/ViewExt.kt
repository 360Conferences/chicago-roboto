package com.chicagoroboto.ext

import android.app.Activity
import android.view.View

fun View.getActivity(): Activity? = if (context is Activity) context as Activity? else null
