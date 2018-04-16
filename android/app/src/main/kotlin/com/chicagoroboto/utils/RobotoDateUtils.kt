package com.chicagoroboto.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedSessionTime(sessionTime: Date?, timezoneName: String): String {
    if (sessionTime == null) {
        return "null"
    }
    else {
        var sdf = SimpleDateFormat("HH:mm a")
        sdf.timeZone = TimeZone.getTimeZone(timezoneName)
        return sdf.format(sessionTime)
    }
}