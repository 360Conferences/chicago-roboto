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

  // We use an indexed map here so we can handle observers that mutate the list
  private var observerIndex = 0
  internal val observers = mutableMapOf<Int, Observer<T>>()

  private var _value: Any? = NOT_SET

  open var value: T
    get() = _value as T
    protected set(v) {
      _value = v
      dispatchValue()
    }

  override val isLive: Boolean
    get() = observers.isNotEmpty()

  override fun onActive() {
  }

  override fun onInactive() {
  }

  private var dispatching = false
  private var invalidated = false

  private fun dispatchValue() {
    if (dispatching) {
      invalidated = true
      return
    }
    dispatching = true
    do {
      invalidated = false
      val value = this.value
      if (value != NOT_SET) {
        // copy the set so it can be mutated
        val keys = observers.keys.toMutableSet()
        for (key in keys) {
          observers[key]?.invoke(value)
        }
      }
    } while (invalidated)
    dispatching = false
  }

  override fun observe(observer: Observer<T>) {
    observers[observerIndex++] = observer

    val value = this.value
    if (value != NOT_SET) {
      observer.invoke(value)
    }

    if (observers.size == 1) {
      onActive()
    }
  }

  override fun removeObserver(observer: Observer<T>) {
    observers.entries.find { it.value == observer }?.let {
      observers.remove(it.key)
    }
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