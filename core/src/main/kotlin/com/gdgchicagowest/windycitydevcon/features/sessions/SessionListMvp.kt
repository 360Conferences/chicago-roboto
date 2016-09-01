package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Session
import com.gdgchicagowest.windycitydevcon.model.Speaker

interface SessionListMvp {

    interface View : Mvp.View {
        fun showNoSessions()
        fun showSessions(sessions: List<Session>)
        fun showSpeakers(speakers: Map<String, Speaker>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setDate(date: String)
    }
}