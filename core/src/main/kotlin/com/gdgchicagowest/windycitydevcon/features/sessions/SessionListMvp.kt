package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Session

interface SessionListMvp {

    interface View : Mvp.View {
        fun showNoSessions()
        fun showSessions(sessions: List<Session>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setDate(date: String)
    }
}