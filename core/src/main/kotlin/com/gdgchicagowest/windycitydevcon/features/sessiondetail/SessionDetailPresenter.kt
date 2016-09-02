package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import com.gdgchicagowest.windycitydevcon.data.SessionProvider
import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import com.gdgchicagowest.windycitydevcon.model.Session
import com.gdgchicagowest.windycitydevcon.model.Speaker

class SessionDetailPresenter(private val sessionProvider: SessionProvider, private val speakerProvider: SpeakerProvider) : SessionDetailMvp.Presenter {

    private var view: SessionDetailMvp.View? = null
    private var session: Session? = null
    private var speakers: MutableSet<Speaker> = mutableSetOf()

    override fun onAttach(view: SessionDetailMvp.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    private val onSpeakerReceived = { speaker: Speaker? ->
        if (speaker != null) {
            speakers.add(speaker)
            view?.showSpeakerInfo(speakers)
        }
    }

    override fun setSessionId(sessionId: String) {
        sessionProvider.addSessionListener(sessionId, { session: Session? ->
            if (session != null) {
                view?.showSessionDetail(session)
                session.speakers?.map {
                    speakerProvider.addSpeakerListener(it, onSpeakerReceived)
                }
            }
        })
    }
}