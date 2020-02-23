package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.SessionDateProvider
import com.chicagoroboto.features.sessions.SessionDatePresenter.Event
import com.chicagoroboto.features.sessions.SessionDatePresenter.Model
import com.chicagoroboto.features.shared.Presenter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SessionDatePresenter @Inject constructor(
    private val sessionDateProvider: SessionDateProvider
) : Presenter<Model, Event> {

  private val idFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
  private val nameFormat = SimpleDateFormat("MMM dd", Locale.US)

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

    launch {
      sessionDateProvider.sessionDates().collect {
        sendModel(model.copy(it.map { Model.SessionDate(it, nameFormat.format(idFormat.parse(it))) }))
      }
    }
  }

  data class Model(
      val dates: List<SessionDate> = emptyList()
  ) {
    data class SessionDate(
        val id: String,
        val name: String
    )
  }

  sealed class Event()
}
