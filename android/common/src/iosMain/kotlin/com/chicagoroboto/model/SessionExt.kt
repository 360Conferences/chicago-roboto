package com.chicagoroboto.model

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

private val shortFormat = NSDateFormatter().apply {
  dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
}
private val longFormat = NSDateFormatter().apply {
  dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
}

val Session.startTime: NSDate?
  get() = if (start_time == null) {
    null
  } else if (longFormat.dateFormat.length == start_time.length) {
    longFormat.dateFromString(start_time)
  } else {
    shortFormat.dateFromString(start_time)
  }

val Session.endTime: NSDate?
  get() = if (end_time == null) {
    null
  } else if (longFormat.dateFormat.length == end_time.length) {
    longFormat.dateFromString(end_time)
  } else {
    shortFormat.dateFromString(end_time)
  }