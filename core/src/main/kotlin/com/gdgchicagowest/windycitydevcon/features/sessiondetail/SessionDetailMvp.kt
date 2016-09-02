package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Session
import com.gdgchicagowest.windycitydevcon.model.Speaker

interface SessionDetailMvp {

    interface View : Mvp.View {
        fun showSessionDetail(session: Session)
        fun showSpeakerInfo(speakers: Set<Speaker>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setSessionId(sessionId: String)
    }
}
