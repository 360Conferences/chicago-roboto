package com.ryanharter.observable

/**
 * An [Observable] that also acts as an [Observer]. This allows chaining
 * of observables.
 *
 * Note: This thread is not thread safe.  All calls to [postValue]
 * must be initiated from the main thread.
 */
open class Subject<X, Y>(private val mapper: (X) -> Y) : Observable<Y>, Observer<X> {

  private val observers = mutableListOf<Observer<Y>>()

  override val isLive: Boolean
    get() = observers.isNotEmpty()

  override fun invoke(value: X) {
    observers.forEach { it.invoke(mapper(value)) }
  }

  override fun onActive() {
  }

  override fun onInactive() {
  }

  override fun observe(observer: Observer<Y>) {
    observers.add(observer)
    if (observers.size == 1) {
      onActive()
    }
  }

  override fun removeObserver(observer: Observer<Y>) {
    observers.remove(observer)
    if (observers.isEmpty()) {
      onInactive()
    }
  }
}