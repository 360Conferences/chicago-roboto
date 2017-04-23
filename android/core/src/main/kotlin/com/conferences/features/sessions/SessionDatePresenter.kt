package com.conferences.features.sessions

import com.conferences.data.SessionDateProvider

class SessionDatePresenter(private val sessionDateProvider: SessionDateProvider) : SessionDateListMvp.Presenter {

    private var view: SessionDateListMvp.View? = null

    override fun onAttach(view: SessionDateListMvp.View) {
        this.view = view

        sessionDateProvider.addSessionDateListener(this, { sessionDates ->
            if (sessionDates == null || sessionDates.isEmpty()) {
                this.view?.showNoSessionDates()
            } else {
                this.view?.let {
                    it.showSessionDates(sessionDates)
                    it.scrollToCurrentDay()
                }
            }
        })
    }

    override fun onDetach() {
        this.view = null
        sessionDateProvider.removeSessionDateListener(this)
    }
}