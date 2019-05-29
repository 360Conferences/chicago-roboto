package com.ryanharter.observable

/**
 * A generic observation API that allows registering [Observer]s
 * to be notified when values are changed.
 */
interface Observable<T> {

  val isLive: Boolean

  fun onActive()
  fun onInactive()

  fun observe(observer: Observer<T>)

  fun removeObserver(observer: Observer<T>)

}

