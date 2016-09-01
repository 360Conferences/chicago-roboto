package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.data.SessionProvider

class SessionListPresenter(private val sessionProvider: SessionProvider) : SessionListMvp.Presenter {

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
    }

    override fun onDetach() {
        this.view = null
        sessionProvider.removeSessionListener(this)
    }
}