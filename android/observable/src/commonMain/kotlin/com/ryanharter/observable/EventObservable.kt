package com.ryanharter.observable

/**
 * An [Observable] that posts values to [Observer]s as they happen.
 * EventObservable doesn't retain values, so new observers will not
 * receive values until a new one is posted.
 *
 * Note: This thread is not thread safe.  All calls to [postValue]
 * must be initiated from the main thread.
 */
open class EventObservable<T> : Observable<T> {

  private val observers = mutableListOf<Observer<T>>()

  override val isLive: Boolean
    get() = observers.isNotEmpty()

  protected open fun postValue(value: T) {
    observers.forEach { it.invoke(value) }
  }

  override fun onActive() {
  }

  override fun onInactive() {
  }

  override fun observe(observer: Observer<T>) {
    observers.add(observer)
    if (observers.size == 1) {
      onActive()
    }
  }

  override fun removeObserver(observer: Observer<T>) {
    observers.remove(observer)
    if (observers.isEmpty()) {
      onInactive()
    }
  }
}

open class MutableEventObservable<T> : EventObservable<T>() {
  public override fun postValue(value: T) {
    super.postValue(value)
  }
}

fun <T> eventObservable(
    onActive: EventObservable<T>.() -> Unit = {},
    onInactive: EventObservable<T>.() -> Unit = {}
): EventObservable<T> =
  object : EventObservable<T>() {
    override fun onActive() {
      onActive.invoke(this)
    }

    override fun onInactive() {
      onInactive.invoke(this)
    }
  }