package com.ryanharter.observable

open class MediatorObservable<T> : DataObservable<T>() {

  private val observers = mutableListOf<Observer<T>>()
  private val sources = mutableMapOf<Observable<*>, Source<*>>()

  override val isLive: Boolean
    get() = observers.isNotEmpty()

  fun <S> addSource(source: Observable<S>, onChange: Observer<S>) {
    val e = Source(source, onChange)
    val existing = sources[source]
    if (existing != null && existing.observer != onChange) {
      throw IllegalArgumentException("This source was already added with a different observer.")
    }
    if (existing != null) {
      return;
    }
    sources[source] = e

    if (isLive) {
      e.plug()
    }
  }

  fun <S> removeSource(source: Observable<S>) {
    sources.remove(source)?.unplug()
  }

  override fun onActive() {
    sources.values.forEach { it.plug() }
  }

  override fun onInactive() {
    sources.values.forEach { it.unplug() }
  }

  private class Source<V>(
      internal val observable: Observable<V>,
      internal val observer: Observer<V>
  ) : Observer<V> {

    fun plug() {
      observable.observe(this)
    }

    fun unplug() {
      observable.removeObserver(this)
    }

    override fun invoke(value: V) {
      observer.invoke(value)
    }
  }
}