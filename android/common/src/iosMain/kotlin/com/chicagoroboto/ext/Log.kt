package com.chicagoroboto.ext

import platform.Foundation.NSLog

actual object Log {
  actual fun log(msg: String) {
    NSLog(msg)
  }
}