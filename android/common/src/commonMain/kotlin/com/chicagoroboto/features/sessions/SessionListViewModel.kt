package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.ryanharter.observable.DataObservable
import com.ryanharter.observable.EventObservable
import com.ryanharter.observable.Observable
import com.ryanharter.observable.dataObservable
import com.ryanharter.observable.map
import com.ryanharter.observable.switchMap

class SessionListViewModel(
    private val sessionProvider: SessionProvider,
    private val speakerProvider: SpeakerProvider,
    private val favoriteProvider: FavoriteProvider
) {

  private data class ViewData(
      val date: String,
      val sessions: List<Session>,
      val speakers: Map<String, Speaker>,
      val favorites: Set<String>
  )

  private val _dateObservable = DataObservable<String>()
  private val _viewDataObservable = _dateObservable.switchMap { date ->
    return@switchMap ViewDataObservable(date)
  }

  private val _viewState = _viewDataObservable.map { data ->
    val sessions = data.sessions.map { s ->
      SessionListViewState.Session(
          title = s.title ?: "TBD",
          room = s.location ?: "TBD",
          speakers = s.speakers?.map { data.speakers[it] }?.map { it?.name ?: "TBD"} ?: emptyList(),
          isFavorite = data.favorites.contains(s.id)
      )
    }
    return@map SessionListViewState(data.date, sessions)
  }
  private val _viewEvents = EventObservable<SessionListViewEvent>()

  val viewState: Observable<SessionListViewState> = _viewState
  val viewEvents: Observable<SessionListViewEvent> = _viewEvents

  fun setDate(date: String) {
    _dateObservable.value = date
  }

  private inner class ViewDataObservable(private val date: String) : DataObservable<ViewData>() {

    private val favoriteKey = "ViewDataObservable$date"

    private var sessions: List<Session>? = null
    private var speakers: Map<String, Speaker>? = null
    private var favorites: Set<String>? = null

    private fun maybeSetValue() {
      val sess = sessions
      val speak = speakers
      val favs = favorites
      if (sess != null && speak != null && favs != null) {
        value = ViewData(date, sess, speak, favs)
      }
    }

    override fun onActive() {
      sessionProvider.addSessionListener(this, date) { sessions ->
        this.sessions = sessions
      }
      speakerProvider.addSpeakerListener(this) { speakers ->
        this.speakers = speakers
      }
      favoriteProvider.addFavoriteListener(favoriteKey) { favorites ->
        this.favorites = favorites
      }
    }

    override fun onInactive() {
      sessionProvider.removeSessionListener(this)
      speakerProvider.removeSpeakerListener(this)
      favoriteProvider.removeFavoriteListener(favoriteKey)
    }
  }
}