package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.data.*
import com.chicagoroboto.ext.guard
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Model.Speaker
import com.chicagoroboto.model.Session
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
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
) {

  data class Model(
      val session: Session = Session(),
      val speakers: List<Speaker> = emptyList(),
      internal val favorites: Set<String> = emptySet()
  ) {
    val isFavorite = favorites.contains(session.id)

    data class Speaker(
        val id: String = "",
        val name: String = "",
        val title: String = "",
        val company: String = "",
        val email: String = "",
        val twitter: String = "",
        val github: String = "",
        val bio: String = "",
        val avatarUrl: String = ""
    )
  }

  private fun speakerToUiSpeaker(speaker: com.chicagoroboto.model.Speaker) = Speaker(
      id = speaker.id ?: "",
      name = speaker.name ?: "",
      title = speaker.title ?: "",
      company = speaker.company ?: "",
      email = speaker.email ?: "",
      twitter = speaker.twitter ?: "",
      github = speaker.github ?: "",
      bio = speaker.bio ?: ""
  )

  private fun List<com.chicagoroboto.model.Speaker>.toUiSpeakers(): List<Speaker> = map {
    Speaker(
        id = it.id ?: "",
        name = it.name ?: "",
        title = it.title ?: "",
        company = it.company ?: "",
        email = it.email ?: "",
        twitter = it.twitter ?: "",
        github = it.github ?: "",
        bio = it.bio ?: "",
        avatarUrl = ""
    )
  }

  sealed class Event {
    data class SetSessionId(val sessionId: String) : Event()
    object ToggleFavorite : Event()
  }

  private val _models = ConflatedBroadcastChannel<Model>()
  val models: Flow<Model> get() = _models.asFlow()

  private val _events = Channel<Event>(BUFFERED)

  suspend fun start() = coroutineScope {
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
                  activeSpeakers = session.speakers ?: emptyList()
                  activeSpeakerJob?.cancel()

                  sendModel(model.copy(
                      speakers = activeSpeakers.map { Speaker(id = it) }
                  ))
                  activeSpeakerJob = launch {
                    combine(activeSpeakers.map(speakerProvider::speaker)) { speakers ->
                      speakers.map(::speakerToUiSpeaker).map { speaker ->
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
  }

  fun onEvent(event: Event) {
    _events.offer(event)
  }
}
