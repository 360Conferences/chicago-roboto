package com.chicagoroboto.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedSessionTime(sessionTime: Date?): String {
    var sdf = SimpleDateFormat("HH:mm a")
    sdf.timeZone = TimeZone.getTimeZone("America/Chicago")
    return sdf.format(sessionTime)
}