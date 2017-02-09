package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.SessionDateProvider

class SessionDatePresenter(private val sessionDateProvider: SessionDateProvider) : SessionDateListMvp.Presenter {

    private var view: SessionDateListMvp.View? = null

    override fun onAttach(view: SessionDateListMvp.View) {
        this.view = view

        sessionDateProvider.addSessionDateListener(this, { sessionDates ->
            if (sessionDates == null || sessionDates.isEmpty()) {
                this.view?.showNoSessionDates()
            } else {
                this.view?.showSessionDates(sessionDates)
            }
        })
    }

    override fun onDetach() {
        this.view = null
        sessionDateProvider.removeSessionDateListener(this)
    }
}