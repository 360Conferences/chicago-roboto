package com.chicagoroboto.features.sessions

import com.chicagoroboto.model.Session

interface SessionNavigator {
    fun showSession(id: String)
}