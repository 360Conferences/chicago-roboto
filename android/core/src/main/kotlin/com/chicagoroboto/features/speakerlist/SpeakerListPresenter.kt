package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.features.shared.Presenter
import com.chicagoroboto.features.speakerlist.SpeakerListPresenter.Event
import com.chicagoroboto.features.speakerlist.SpeakerListPresenter.Model
import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SpeakerListPresenter @Inject constructor(
    private val speakerProvider: SpeakerProvider
) : Presenter<Model, Event> {

  private val _models = ConflatedBroadcastChannel<Model>()
  override val models: Flow<Model> get() = _models.asFlow()

  private val _events = Channel<Event>(RENDEZVOUS)
  override val events: SendChannel<Event> get() = _events

  override suspend fun start() = coroutineScope {
    var model = Model()
    fun sendModel(newModel: Model) {
      model = newModel
      _models.offer(newModel)
    }

    launch {
      speakerProvider.speakers().collect { speakers ->
        sendModel(model.copy(speakers = speakers))

        // Asynchronously load the avatar urls
        val withAvatars = speakers.map { speaker ->
          async(Dispatchers.Default) {
            speaker.copy(avatarUrl = speakerProvider.avatar(speaker.id))
          }
        }.awaitAll()
        sendModel(model.copy(speakers = withAvatars))
      }
    }

    Unit
  }

  data class Model(
      val speakers: List<Speaker> = emptyList()
  )

  sealed class Event {

  }

}
