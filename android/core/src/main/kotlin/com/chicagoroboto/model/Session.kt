package com.chicagoroboto.model

import sun.reflect.generics.reflectiveObjects.NotImplementedException
import java.text.DateFormat
import java.text.FieldPosition
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date

class Session(val id: String? = null,
              val title: String? = null,
              val abstract: String? = null,
              val start_time: String? = null,
              val end_time: String? = null,
              val date: String? = null,
              val speakers: List<String>? = null,
              val room: String? = "Main",
              val tracks: List<String>? = null) {

    private val format = object : DateFormat() {
        private val format1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        private val format2 = "yyyy-MM-dd'T'HH:mm:ssZ"
        private val df1 = SimpleDateFormat(format1)
        private val df2 = SimpleDateFormat(format2)
        override fun parse(source: String, pos: ParsePosition): Date {
            if (source.length - pos.index == format1.length) {
                return df1.parse(source, pos)
            } else {
                return df2.parse(source, pos)
            }
        }

        override fun format(date: Date?, p1: StringBuffer?, p2: FieldPosition?): StringBuffer {
            throw NotImplementedException()
        }

    }

    val startTime: Date?
        get() = format.parse(start_time)

    val endTime: Date?
        get() = format.parse(end_time)
}