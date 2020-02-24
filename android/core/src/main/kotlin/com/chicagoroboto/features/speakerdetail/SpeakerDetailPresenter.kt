package com.chicagoroboto.features.speakerdetail

import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.features.shared.Presenter
import com.chicagoroboto.features.speakerdetail.SpeakerDetailPresenter.Event
import com.chicagoroboto.features.speakerdetail.SpeakerDetailPresenter.Event.SetSpeakerId
import com.chicagoroboto.features.speakerdetail.SpeakerDetailPresenter.Model
import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpeakerDetailPresenter @Inject constructor(
    private val speakerProvider: SpeakerProvider
) : Presenter<Model, Event> {

  private val _models = ConflatedBroadcastChannel<Model>()
  override val models: Flow<Model> get() = _models.asFlow()

  private val _events = Channel<Event>(BUFFERED)
  override val events: SendChannel<Event> get() = _events

  override suspend fun start() = coroutineScope<Unit> {
    var model = Model()
    fun sendModel(newModel: Model) {
      model = newModel
      _models.offer(newModel)
    }

    launch {
      var activeSpeakerId: String = ""
      var activeSpeakerJob: Job? = null

      _events.consumeEach { event ->
        when (event) {
          is SetSpeakerId -> {
            val speakerId = event.id
            if (speakerId != activeSpeakerId) {
              activeSpeakerId = speakerId
              activeSpeakerJob?.cancel()

              activeSpeakerJob = launch {
                speakerProvider.speaker(activeSpeakerId).collect {
                  val speaker = it.copy(avatarUrl = speakerProvider.avatar(it.id))
                  sendModel(model.copy(speaker = speaker))
                }
              }
            }
          }
        }
      }
    }
  }

  data class Model(
      val speaker: Speaker = Speaker("")
  )

  sealed class Event {
    data class SetSpeakerId(val id: String) : Event()
  }
}
