package com.conferences.features.sessions

import com.conferences.data.FavoriteProvider
import com.conferences.data.SessionProvider
import com.conferences.data.SpeakerProvider
import com.conferences.model.Session
import java.util.*

class SessionListPresenter(private val sessionProvider: SessionProvider,
                           private val speakerProvider: SpeakerProvider,
                           private val favoriteProvider: FavoriteProvider)
        : SessionListMvp.Presenter {

    private var view: SessionListMvp.View? = null
    private var date: String? = null

    override fun onAttach(view: SessionListMvp.View) {
        this.view = view
    }

    override fun setDate(date: String) {
        sessionProvider.addSessionListener(this, date, { sessions ->
            if (sessions == null || sessions.isEmpty()) {
                this.view?.showNoSessions()
            } else {
                this.view?.let {
                    it.showSessions(sessions)
                    it.scrollTo(findCurrentSessionIndex(sessions))
                }
            }
        })
        speakerProvider.addSpeakerListener(this, { speakers ->
            if (speakers != null && speakers.isNotEmpty()) {
                this.view?.showSpeakers(speakers)
            }
        })
        favoriteProvider.removeFavoriteListener(date)
        favoriteProvider.addFavoriteListener(date, { sessions ->
            this.view?.showFavorites(sessions)
        })
    }

    override fun onDetach() {
        this.view = null
        sessionProvider.removeSessionListener(this)
        date?.let {
            favoriteProvider.removeFavoriteListener(it)
        }
    }

    private fun findCurrentSessionIndex(sessions: List<Session>): Int {
        // find the current session index
        val now = Date()
        val index = sessions.indexOfFirst { now.after(it.startTime) && now.before(it.endTime) }
        return index
    }
}