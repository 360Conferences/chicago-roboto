package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.NotificationProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker

class SessionDetailPresenter(private val sessionProvider: SessionProvider,
                             private val speakerProvider: SpeakerProvider,
                             private val favoriteProvider: FavoriteProvider,
                             private val notificationProvider: NotificationProvider) : SessionDetailMvp.Presenter {

    private var sessionId: String? = null
    private var session: Session? = null
    private var view: SessionDetailMvp.View? = null
    private var speakers: MutableSet<Speaker> = mutableSetOf()

    private var isFavorite: Boolean = false
        set(value) {
            field = value
            view?.setIsFavorite(value)
        }

    override fun onAttach(view: SessionDetailMvp.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
        removeListeners()
    }

    private fun removeListeners() {
        session?.let {
            it.speakers?.map {
                speakerProvider.removeSpeakerListener(it)
            }
        }
        sessionId?.let {
            sessionProvider.removeSessionListener(it)
        }
    }

    override fun setSessionId(sessionId: String) {
        removeListeners()

        this.sessionId = sessionId
        sessionProvider.addSessionListener(sessionId, { session: Session? ->
            this.session = session
            if (session != null) {
                view?.showSessionDetail(session)
                session.speakers?.map {
                    speakerProvider.addSpeakerListener(it, onSpeakerReceived)
                }
            }
        })

        favoriteProvider.addFavoriteListener(sessionId, { sessions ->
            isFavorite = sessions.contains(sessionId)
        })
    }

    private val onSpeakerReceived = { speaker: Speaker? ->
        if (speaker != null) {
            speakers.add(speaker)
            view?.showSpeakerInfo(speakers)
        }
    }

    override fun toggleFavorite() {
        sessionId?.let {
            if (isFavorite) {
                favoriteProvider.removeFavoriteSession(it)
                session?.let { notificationProvider.unscheduleFeedbackNotification(it) }
            } else {
                favoriteProvider.addFavoriteSession(it)
                session?.let { notificationProvider.scheduleFeedbackNotification(it) }
            }
        }
    }

}