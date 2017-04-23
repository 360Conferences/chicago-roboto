package com.conferences.data

interface SessionDateProvider {
    fun addSessionDateListener(key: Any, onComplete: (sessionDates: List<String>?) -> Unit)
    fun removeSessionDateListener(key: Any)
}