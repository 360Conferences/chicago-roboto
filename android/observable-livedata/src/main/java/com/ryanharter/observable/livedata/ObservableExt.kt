package com.ryanharter.observable.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ryanharter.observable.DataObservable
import com.ryanharter.observable.Observable

// asLiveData is only available on DataObservable that is the only Observable which guarantees
// that it won't break the LiveData contract (storing values).
/**
 * Creates a [LiveData] that allows for Lifecycle aware observation and transformation.
 */
fun <T> DataObservable<T>.asLiveData(): LiveData<T> = ObservableLiveData(this)

/**
 * Adds the given observer to the observers list within the lifespan of the given owner. The
 * events are dispatched on the main thread.
 *
 * The observer will only receive events if the owner is in the
 * [androidx.lifecycle.Lifecycle.State.STARTED] or [androidx.lifecycle.Lifecycle.State.RESUMED]
 * state (active).
 */
fun <T> Observable<T>.observe(owner: LifecycleOwner, observer: Observer<T>)
    = ObservableLiveData(this).observe(owner, observer)

/**
 * [LiveData] that wraps an [Observable], allowing lifecycle aware observation.
 *
 * The LiveData internals are used for lifecycle management, but the underlying observable's
 * state management mechanisms are used to determine the values that [Observer]s should
 * receive since observables aren't guaranteed to store existing state.
 */
internal class ObservableLiveData<T>(
    private val observable: Observable<T>
) : LiveData<T>() {

  private val observers = mutableMapOf<Observer<in T>, StartVersionObserver>()
  private val wrappedObservers = mutableMapOf<StartVersionObserver, Observer<in T>>()
  private var version = 0

  // Observes the underlying Observable and sets this value accordingly
  private val internalObserver: com.ryanharter.observable.Observer<T> = { value = it }

  override fun onActive() {
    super.onActive()
    observable.observe(internalObserver)
  }

  override fun onInactive() {
    observable.removeObserver(internalObserver)
    super.onInactive()
  }

  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    val wrapped = observers[observer] ?: StartVersionObserver(version, observer).also {
      observers[observer] = it
      wrappedObservers[it] = observer
    }
    super.observe(owner, wrapped)
  }

  override fun observeForever(observer: Observer<in T>) {
    val wrapped = observers[observer] ?: StartVersionObserver(version, observer).also {
      observers[observer] = it
      wrappedObservers[it] = observer
    }
    super.observeForever(wrapped)
  }

  override fun removeObserver(observer: Observer<in T>) {
    // since we don't know where this will be called from, `observer` could be
    // the original observer or the wrapped observer, so we need to make sure
    // we update our current observer state and pass the wrapped observer to the
    // super.
    val originalObserver = wrappedObservers.remove(observer) ?: observer
    val wrapped = observers.remove(originalObserver) ?: return
    if (observer == originalObserver) wrappedObservers.remove(wrapped)
    super.removeObserver(wrapped)
  }

  override fun setValue(value: T?) {
    version++
    super.setValue(value)
  }

  internal inner class StartVersionObserver(
      private var lastSeenVersion: Int,
      private val observer: Observer<in T>) : Observer<T> {

    override fun onChanged(t: T?) {
      if (lastSeenVersion < this@ObservableLiveData.version) {
        lastSeenVersion = this@ObservableLiveData.version
        observer.onChanged(t)
      }
    }
  }
}