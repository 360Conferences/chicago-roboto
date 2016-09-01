package com.gdgchicagowest.windycitydevcon.data

import com.gdgchicagowest.windycitydevcon.model.Session

interface SessionProvider {
    fun addSessionListener(key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit)
    fun removeSessionListener(key: Any)
}