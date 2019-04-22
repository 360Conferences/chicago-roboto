package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Session
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
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

      launch {
        val speakers = speakerProvider.getSpeakersMap(this)
        withContext(UI) {
          this@SessionListPresenter.view?.showSpeakers(speakers)
        }
      }
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