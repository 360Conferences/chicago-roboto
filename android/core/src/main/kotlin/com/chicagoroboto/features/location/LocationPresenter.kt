package com.chicagoroboto.features.location

import com.chicagoroboto.data.VenueProvider
import com.chicagoroboto.features.location.LocationPresenter.Event
import com.chicagoroboto.features.location.LocationPresenter.Model
import com.chicagoroboto.features.shared.Presenter
import com.chicagoroboto.model.Venue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationPresenter @Inject constructor(
    private val venueProvider: VenueProvider
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

    launch {
      venueProvider.venue().collect {
        sendModel(model.copy(venue = it))
      }
    }
  }

  data class Model(
      val venue: Venue = Venue("Conference Center")
  )

  sealed class Event
}
