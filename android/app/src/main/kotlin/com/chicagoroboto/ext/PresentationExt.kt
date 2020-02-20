package com.chicagoroboto.ext

import androidx.lifecycle.*
import com.chicagoroboto.features.shared.Presentation


class PresenterViewModel<P : Presentation<*>>(
    val presentation: P
) : ViewModel() {

  class Factory<P : Presentation<*>>(val creator: () -> P) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        PresenterViewModel(creator()) as? T ?: throw IllegalArgumentException("Unknown type")
  }

  override fun onCleared() {
    super.onCleared()
    presentation.stop()
  }
}

/**
 * Returns the retained [Presentation], or uses [creator] to create one if one doesn't exist.
 *
 * ```
 * class MyFragment : Fragment() {
 *   @Inject lateinit var myPresenterProvider: Provider<MyPresenter>
 *
 *   private val presentation: Presentation<MyPresenter>
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *     super.onCreate(savedInstanceState)
 *     component.inject(this)
 *
 *     presentation = retainedPresentation {
 *       presenterProvider.get().startPresentation(Dispatchers.Main)
 *     }
 *     val presenter = presentation.presenter
 *
 *   }
 * }
 * ```
 */
fun <P : Presentation<*>> ViewModelStoreOwner.retainedPresentation(creator: () -> P): P {
  return ViewModelProvider(this, PresenterViewModel.Factory(creator))
      .get<PresenterViewModel<P>>()
      .presentation
}

/**
 * Returns a property delegate to access the retained [Presentation]. [creator] will be used to
 * create the Presentation the first time.
 *
 * ```
 * class MyFragment : Fragment() {
 *   @Inject lateinit var myPresenterProvider: Provider<MyPresenter>
 *
 *   private val presentation by presentations {
 *     presenterProvider.get().startPresentation(Dispatchers.Main)
 *   }
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *     super.onCreate(savedInstanceState)
 *     component.inject(this)
 *     presentation.presenter.models.collect {
 *       // ...
 *     }
 *   }
 * }
 * ```
 */
inline fun <reified P : Presentation<*>> ViewModelStoreOwner.presentations(noinline creator: () -> P): Lazy<P> {
  return LazyPresentation({ viewModelStore }, { PresenterViewModel.Factory(creator) })
}

class LazyPresentation<P : Presentation<*>>(
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<P> {
  private var cached: P? = null

  override val value: P
    get() {
      val presentation = cached
      return if (presentation == null) {
        val factory = factoryProducer()
        val store = storeProducer()
        ViewModelProvider(store, factory).get<PresenterViewModel<P>>().presentation.also {
          cached = it
        }
      } else {
        presentation
      }
    }

  override fun isInitialized() = cached != null
}
