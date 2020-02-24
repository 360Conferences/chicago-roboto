package com.chicagoroboto.features.info

import com.chicagoroboto.data.LibraryProvider
import com.chicagoroboto.features.info.InfoPresenter.Event
import com.chicagoroboto.features.info.InfoPresenter.Event.ClickedLibrary
import com.chicagoroboto.features.info.InfoPresenter.Model
import com.chicagoroboto.features.shared.Presenter
import com.chicagoroboto.model.Library
import com.chicagoroboto.navigator.WebNavigator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfoPresenter @Inject constructor(
    private val libraryProvider: LibraryProvider,
    private val webNavigator: WebNavigator
) : Presenter<Model, Event> {

  private val _models = ConflatedBroadcastChannel<Model>()
  override val models: Flow<Model> get() = _models.asFlow()

  private val _events = Channel<Event>(Channel.BUFFERED)
  override val events: SendChannel<Event> get() = _events

  override suspend fun start() = coroutineScope<Unit> {
    var model = Model()
    fun sendModel(newModel: Model) {
      model = newModel
      _models.offer(model)
    }

    launch {
      libraryProvider.libraries().collect {
        sendModel(model.copy(libraries = it))
      }
    }

    launch {
      _events.consumeEach { event ->
        when (event) {
          is ClickedLibrary -> {
            webNavigator.navigateToUrl(event.library.url)
          }
        }
      }
    }
  }

  data class Model(
      val libraries: List<Library> = emptyList()
  )

  sealed class Event {
    data class ClickedLibrary(val library: Library) : Event()
  }
}
