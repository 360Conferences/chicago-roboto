package com.ryanharter.observable

/**
 * An [Observable] that remembers the last value supplied and
 * provides it to new [Observer]s as they are added.
 *
 * Note: This thread is not thread safe.  All calls to [value]
 * must be initiated from the main thread.
 */
@Suppress("UNCHECKED_CAST")
open class DataObservable<T> : Observable<T> {

  private val observers = mutableListOf<Observer<T>>()

  private var _value: Any? = NOT_SET

  var value: T
    get() = _value as T
    set(v) {
      _value = v
      dispatchValue()
    }

  override val isLive: Boolean
    get() = observers.isNotEmpty()

  override fun onActive() {
  }

  override fun onInactive() {
  }

  private fun dispatchValue() {
    val value = this.value
    if (value != NOT_SET) {
      observers.forEach { it.invoke(value) }
    }
  }

  override fun observe(observer: Observer<T>) {
    observers.add(observer)

    val value = this.value
    if (value != NOT_SET) {
      observer.invoke(value)
    }

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

  companion object {
    private val NOT_SET = Any()
  }
}

fun <T> dataObservable(
    onActive: DataObservable<T>.() -> Unit = {},
    onInactive: DataObservable<T>.() -> Unit = {}
): DataObservable<T> =
    object : DataObservable<T>() {
      override fun onActive() {
        onActive.invoke(this)
      }

      override fun onInactive() {
        onInactive.invoke(this)
      }
    }