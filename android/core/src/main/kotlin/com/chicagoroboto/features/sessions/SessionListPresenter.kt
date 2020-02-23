package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.data.UserProvider
import com.chicagoroboto.features.sessions.SessionListPresenter.Event
import com.chicagoroboto.features.sessions.SessionListPresenter.Event.SetSessionDate
import com.chicagoroboto.features.sessions.SessionListPresenter.Model
import com.chicagoroboto.features.sessions.SessionListPresenter.Model.Session
import com.chicagoroboto.features.shared.Presenter
import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import com.chicagoroboto.model.Session as ApiSession

class SessionListPresenter @Inject constructor(
    private val sessionProvider: SessionProvider,
    private val speakerProvider: SpeakerProvider,
    private val favoriteProvider: FavoriteProvider,
    private val userProvider: UserProvider
) : Presenter<Model, Event> {

  private val _models = ConflatedBroadcastChannel<Model>()
  override val models: Flow<Model> get() = _models.asFlow()

  private val _events = Channel<Event>(BUFFERED)
  override val events: SendChannel<Event> get() = _events

  override suspend fun start() = coroutineScope<Unit> {
    var model = Model()
    fun sendModel(newModel: Model) {
      model = newModel
      _models.offer(model)
    }

    val allSpeakersFlow = speakerProvider.speakers()
    val favoritesFlow =
        userProvider.currentUser?.let { favoriteProvider.favorites(it.id) } ?: flowOf(emptySet())

    launch {
      var activeDate = ""
      var activeDateJob: Job? = null

      _events.consumeEach { event ->
        when (event) {
          is SetSessionDate -> {
            val sessionDate = event.date

            if (sessionDate != activeDate) {
              activeDate = sessionDate
              activeDateJob?.cancel()

              activeDateJob = launch {
                val sessionsByDateFlow = sessionProvider.sessionsByDate(activeDate)

                combine(sessionsByDateFlow, allSpeakersFlow, favoritesFlow) { sessions, speakers, favorites ->
                  val speakersById = speakers.associateBy { it.id }
                  sessions.map { s ->
                    Session(s, s.speakers.mapNotNull { speakersById[it] }, favorites.contains(s.id))
                  }
                }.collect { sessions ->
                  sendModel(model.copy(
                      sessions = sessions,
                      currentSessionIndex = findCurrentSessionIndex(sessions)
                  ))
                }
              }
            }
          }
        }
      }
    }
  }

  private fun findCurrentSessionIndex(sessions: List<Session>): Int {
    val now = Date()
    return sessions.indexOfFirst {
      it.session.startTime?.after(now) == true && it.session.endTime?.before(now) == true
    }
  }

  data class Model(
      val sessions: List<Session> = emptyList(),
      val currentSessionIndex: Int = -1
  ) {
    data class Session(
        val session: ApiSession = ApiSession(""),
        val speakers: List<Speaker> = emptyList(),
        val isFavorite: Boolean = false
    )
  }

  sealed class Event() {
    data class SetSessionDate(val date: String) : Event()
  }
}
