package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker

interface SessionDetailMvp {

    interface View : Mvp.View {
        fun showSessionDetail(session: Session)
        fun showSpeakerInfo(speakers: Set<Speaker>)
        fun setIsFavorite(isFavorite: Boolean)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setSessionId(sessionId: String)
        fun toggleFavorite()
    }
}
