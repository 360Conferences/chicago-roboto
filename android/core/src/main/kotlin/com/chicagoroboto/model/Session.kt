package com.chicagoroboto.model

import sun.reflect.generics.reflectiveObjects.NotImplementedException
import timber.log.Timber
import timber.log.error
import java.text.*
import java.util.*

data class Session(
    val id: String,
    val type: String = "",
    val title: String = "",
    val description: String = "",
    val start_time: String = "",
    val end_time: String = "",
    val date: String = "",
    val speakers: List<String> = emptyList(),
    val location: String = "Main",
    val address: String = "",
    val tracks: List<String> = emptyList()
) {

  private val format = object : DateFormat() {
    private val format1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    private val format2 = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val df1 = SimpleDateFormat(format1)
    private val df2 = SimpleDateFormat(format2)
    override fun parse(source: String?, pos: ParsePosition?): Date? {
      return if (source == null || pos == null) {
        null
      } else if (source.length - pos.index == format1.length) {
        df1.parse(source, pos)
      } else {
        df2.parse(source, pos)
      }
    }

    override fun format(date: Date?, toAppendTo: StringBuffer?, fieldPosition: FieldPosition?): StringBuffer {
      throw NotImplementedException()
    }
  }

  val startTime: Date?
    get() {
      return try {
        format.parse(start_time)
      } catch (e: ParseException) {
        Timber.error(e) { "Failed to parse start_time: $start_time" }
        null
      }
    }

  val endTime: Date?
    get() {
      return try {
        format.parse(end_time)
      } catch (e: ParseException) {
        Timber.error(e) { "Failed to parse end_time: $end_time" }
        null
      }
    }
}
