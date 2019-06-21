package com.chicagoroboto.ext

import android.util.Log

actual object Log {
  actual fun log(msg: String) {
    Log.d("ROBOTO", msg)
  }
}