package com.ryanharter.observable.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ryanharter.observable.DataObservable

internal val lock = Any()
internal val registry = mutableMapOf<DataObservable<*>, ObservableLiveData<*>>()

fun <T> DataObservable<T>.asLiveData(): LiveData<T> {
  return synchronized(lock) {
    @Suppress("UNCHECKED_CAST")
    registry.getOrPut(this) { ObservableLiveData(this) } as ObservableLiveData<T>
  }
}

fun <T> DataObservable<T>.observe(owner: LifecycleOwner, observer: Observer<T>) =
  this.asLiveData().observe(owner, observer)

internal class ObservableLiveData<T>(private val observable: DataObservable<T>) : LiveData<T>() {

  private val observer: com.ryanharter.observable.Observer<T> = { value = it }

  override fun onActive() {
    super.onActive()
    observable.observe(observer)
  }

  override fun onInactive() {
    observable.removeObserver(observer)
    super.onInactive()
  }
}