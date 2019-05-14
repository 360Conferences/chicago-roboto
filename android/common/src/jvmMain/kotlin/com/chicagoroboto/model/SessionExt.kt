package com.chicagoroboto.model

import java.text.DateFormat
import java.text.FieldPosition
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date

private val format: DateFormat = object : DateFormat() {
  private val format1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  private val format2 = "yyyy-MM-dd'T'HH:mm:ssZ"
  private val df1 = SimpleDateFormat(format1)
  private val df2 = SimpleDateFormat(format2)
  override fun parse(source: String, pos: ParsePosition): Date {
    return if (source.length - pos.index == format1.length) {
      df1.parse(source, pos)
    } else {
      df2.parse(source, pos)
    }
  }

  override fun format(date: Date?, p1: StringBuffer?, p2: FieldPosition?): StringBuffer {
    TODO("not implemented")
  }
}

val Session.startTime: Date?
  get() = format.parse(start_time)

val Session.endTime: Date?
  get() = format.parse(end_time)