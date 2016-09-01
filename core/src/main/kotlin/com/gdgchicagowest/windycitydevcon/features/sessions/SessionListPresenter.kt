package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.data.SessionProvider
import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider

class SessionListPresenter(private val sessionProvider: SessionProvider, private val speakerProvider: SpeakerProvider)
        : SessionListMvp.Presenter {

    private var view: SessionListMvp.View? = null

    override fun onAttach(view: SessionListMvp.View) {
        this.view = view
    }

    override fun setDate(date: String) {
        sessionProvider.addSessionListener(this, date, { sessions ->
            if (sessions == null || sessions.isEmpty()) {
                this.view?.showNoSessions()
            } else {
                this.view?.showSessions(sessions)
            }
        })
        speakerProvider.addSpeakerListener(this, { speakers ->
            if (speakers != null && speakers.isNotEmpty()) {
                this.view?.showSpeakers(speakers)
            }
        })
    }

    override fun onDetach() {
        this.view = null
        sessionProvider.removeSessionListener(this)
    }
}