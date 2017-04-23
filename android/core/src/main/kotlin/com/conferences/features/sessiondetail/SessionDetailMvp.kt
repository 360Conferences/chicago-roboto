package com.conferences.features.sessiondetail

import com.conferences.features.shared.Mvp
import com.conferences.model.Session
import com.conferences.model.Speaker

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
