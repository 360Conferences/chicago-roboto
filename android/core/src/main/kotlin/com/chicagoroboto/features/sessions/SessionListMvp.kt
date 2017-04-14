package com.chicagoroboto.features.sessions

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker

interface SessionListMvp {

    interface View : Mvp.View {
        fun showNoSessions()
        fun showSessions(sessions: List<Session>)
        fun showSpeakers(speakers: Map<String, Speaker>)
        fun showFavorites(favorites: Set<String>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setDate(date: String)
    }
}