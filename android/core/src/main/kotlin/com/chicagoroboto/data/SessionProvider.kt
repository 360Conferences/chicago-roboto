package com.chicagoroboto.data

import com.chicagoroboto.model.Session

interface SessionProvider {
    fun addSessionListener(id: String, onComplete: (Session?) -> Unit)
    fun addSessionListener(key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit)
    fun removeSessionListener(key: Any)
}