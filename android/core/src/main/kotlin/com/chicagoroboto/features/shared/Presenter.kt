package com.chicagoroboto.features.shared

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow

interface Presenter<ModelT : Any, EventT : Any> {
  val models: Flow<ModelT>
  val events: SendChannel<EventT>
  suspend fun start()
}
