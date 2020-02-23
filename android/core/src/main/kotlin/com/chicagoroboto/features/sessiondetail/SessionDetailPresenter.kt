package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.data.*
import com.chicagoroboto.ext.guard
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Event
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Model
import com.chicagoroboto.features.shared.Presenter
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import timber.log.debug
import timber.log.error
import javax.inject.Inject

class SessionDetailPresenter @Inject constructor(
    private val sessionProvider: SessionProvider,
    private val speakerProvider: SpeakerProvider,
    private val favoriteProvider: FavoriteProvider,
    private val userProvider: UserProvider,
    private val notificationProvider: NotificationProvider
) : Presenter<Model, Event> {

  private val _models = ConflatedBroadcastChannel<Model>()
  override val models: Flow<Model> get() = _models.asFlow()

  private val _events = Channel<Event>(BUFFERED)
  override val events: SendChannel<Event> get() = _events

  override suspend fun start() = coroutineScope {
    var model = Model()
    fun sendModel(newModel: Model) {
      model = newModel
      _models.offer(newModel)
    }

    var favorites = emptySet<String>()
    launch {
      val userId = userProvider.currentUser?.id ?: userProvider.signIn()?.id guard {
        Timber.error { "Unable to get user" }
        // Todo disable the favorite button
        return@launch
      }
      favoriteProvider.favorites(userId).collect {
        if (it != favorites) {
          favorites = it
          sendModel(model.copy(favorites = it))
        }
      }
    }

    launch {
      var activeSessionId = ""
      var activeSessionListenerJob: Job? = null

      _events.consumeEach {
        when (it) {
          is Event.SetSessionId -> {
            val sessionId = it.sessionId
            if (sessionId == activeSessionId) return@consumeEach

            activeSessionId = sessionId
            activeSessionListenerJob?.cancel()

            activeSessionListenerJob = launch {
              var activeSpeakers = emptyList<String>()
              var activeSpeakerJob: Job? = null

              sessionProvider.session(sessionId).collect { session ->
                // Send the session ASAP with placeholder speakers
                sendModel(model.copy(
                    session = session
                ))

                if (session.speakers != activeSpeakers) {
                  activeSpeakers = session.speakers
                  activeSpeakerJob?.cancel()

                  sendModel(model.copy(
                      speakers = activeSpeakers.map { Speaker(id = it) }
                  ))
                  activeSpeakerJob = launch {
                    combine(activeSpeakers.map(speakerProvider::speaker)) { speakers ->
                      speakers.map { speaker ->
                        // Asynchronously fetch all of the speaker avatars
                        async(Dispatchers.Default) {
                          speaker.copy(avatarUrl = speakerProvider.avatar(speaker.id))
                        }
                      }.awaitAll()
                    }.collect { sendModel(model.copy(speakers = it))}
                  }
                }
              }
            }
          }
          Event.ToggleFavorite -> {
            val userId = userProvider.currentUser?.id ?: userProvider.signIn()?.id guard {
              Timber.error { "Unable to get user" }
              return@consumeEach
            }
            val sessionId = activeSessionId guard {
              Timber.debug { "Attempted to favorite with missing session id." }
              return@consumeEach
            }

            if (!model.isFavorite) {
              favoriteProvider.addFavoriteSession(userId, sessionId)
              if (activeSessionId.isNotBlank()) {
                notificationProvider.scheduleFeedbackNotification(model.session)
              }
            } else {
              favoriteProvider.removeFavoriteSession(userId, sessionId)
              if (activeSessionId.isNotBlank()) {
                notificationProvider.unscheduleFeedbackNotification(model.session)
              }
            }
          }
        }
      }
    }
    Unit
  }

  data class Model(
      val session: Session = Session(""),
      val speakers: List<Speaker> = emptyList(),
      internal val favorites: Set<String> = emptySet()
  ) {
    val isFavorite = favorites.contains(session.id)
  }

  sealed class Event {
    data class SetSessionId(val sessionId: String) : Event()
    object ToggleFavorite : Event()
  }
}
