package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Session

class SessionListPresenter(
    private val sessionProvider: SessionProvider,
    private val speakerProvider: SpeakerProvider,
    private val favoriteProvider: FavoriteProvider
) : SessionListMvp.Presenter {

  private var view: SessionListMvp.View? = null
  private var date: String? = null

  override fun onAttach(view: SessionListMvp.View) {
    this.view = view
  }

  override fun setDate(date: String) {
    sessionProvider.addSessionListener(this, date) { sessions ->
      if (sessions == null || sessions.isEmpty()) {
        this.view?.showNoSessions()
      } else {
        this.view?.let {
          it.showSessions(sessions)
//          it.scrollTo(findCurrentSessionIndex(sessions))
        }
      }
    }
    speakerProvider.addSpeakerListener(this) { speakers ->
      if (speakers != null && speakers.isNotEmpty()) {
        this.view?.showSpeakers(speakers)
      }
    }
    favoriteProvider.removeFavoriteListener(date)
    favoriteProvider.addFavoriteListener(date) { sessions ->
      this.view?.showFavorites(sessions)
    }
  }

  override fun onDetach() {
    this.view = null
    sessionProvider.removeSessionListener(this)
    date?.let {
      favoriteProvider.removeFavoriteListener(it)
    }
  }
}
