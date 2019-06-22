package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.ext.Log
import com.chicagoroboto.features.shared.ViewModel
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combineLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.reactive.flow.asPublisher

class SessionListViewModel(
    private val sessionProvider: SessionProvider,
    private val speakerProvider: SpeakerProvider,
    private val favoriteProvider: FavoriteProvider
) : ViewModel() {

  @ExperimentalCoroutinesApi
  private val dateChannel = ConflatedBroadcastChannel<String>()
  @FlowPreview
  @ExperimentalCoroutinesApi
  private val sessionFlow: Flow<List<Session>> = dateChannel
      .asFlow()
      .flatMapConcat { sessionProvider.sessionsForDate(it) }
  @ExperimentalCoroutinesApi
  private val speakerFlow: Flow<List<Speaker>> = speakerProvider.getSpeakers()
  @ExperimentalCoroutinesApi
  private val favoriteFlow: Flow<Set<String>> = favoriteProvider.getFavorites()
  @ExperimentalCoroutinesApi
  @FlowPreview
  val viewState = dateChannel.asFlow()
      .combineLatest(sessionFlow, speakerFlow, favoriteFlow) { date, sessions, speakers, favorites ->
        val viewSessions = sessions.map { s ->
          SessionListViewState.Session(
            id = s.id ?: "unknown",
            title = s.title ?: "TBD",
            room = s.location ?: "TBD",
            speakers = s.speakers
                ?.map { speakers.find { speaker -> speaker.id == it } }
                ?.map { it?.name ?: "TBD"}
                ?: emptyList(),
            isFavorite = favorites.contains(s.id)
          )
        }
        SessionListViewState(date, viewSessions)
      }
      .asPublisher()

  @ExperimentalCoroutinesApi
  private val eventChannel = BroadcastChannel<SessionListViewEvent>(BUFFERED)
  @ExperimentalCoroutinesApi
  @FlowPreview
  val viewEvents = eventChannel.asFlow().asPublisher()

  @ExperimentalCoroutinesApi
  fun setDate(date: String) {
    dateChannel.offer(date)
  }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
internal expect fun SessionListViewModel.findCurrentSessionIndex(sessions: List<Session>): Int