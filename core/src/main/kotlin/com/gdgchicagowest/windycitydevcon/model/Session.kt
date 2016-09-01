package com.gdgchicagowest.windycitydevcon.model

import java.text.SimpleDateFormat
import java.util.*

class Session(val name: String? = null, val description: String? = null,
              val start_time: String? = null, val end_time: String? = null, val date: String? = null,
              val speakers: List<String>? = null, val room: String? = null,
              val tracks: List<String>? = null) {

    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    val startTime: Date?
        get() = format.parse(start_time)

    val endTime: Date?
        get() = format.parse(end_time)
}