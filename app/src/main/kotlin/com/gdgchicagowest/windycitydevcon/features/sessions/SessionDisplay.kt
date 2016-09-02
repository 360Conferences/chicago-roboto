package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.model.Session

interface SessionDisplay {
    fun showSession(session: Session)
}