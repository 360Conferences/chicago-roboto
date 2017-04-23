package com.conferences.features.sessions

import com.conferences.features.shared.Mvp
import com.conferences.model.Session
import com.conferences.model.Speaker

interface SessionListMvp {

    interface View : Mvp.View {
        fun showNoSessions()
        fun showSessions(sessions: List<Session>)
        fun showSpeakers(speakers: Map<String, Speaker>)
        fun showFavorites(favorites: Set<String>)
        fun scrollTo(index: Int)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setDate(date: String)
    }
}