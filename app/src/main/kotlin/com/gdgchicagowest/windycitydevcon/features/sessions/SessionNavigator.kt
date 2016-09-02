package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.model.Session

interface SessionNavigator {
    fun showSession(session: Session)
}