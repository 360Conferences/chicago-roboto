package com.ryanharter.observable

//fun <X, Y> DataObservable<X>.safeMap(mapFunc: (X) -> Y?)

fun <X, Y> DataObservable<X>.map(mapFunc: (X?) -> Y?): DataObservable<Y> =
  MediatorDataObservable<Y>().apply {
    addSource(this@map) { value = mapFunc(it) }
  }

fun <X, Y> DataObservable<X>.switchMap(mapFunc: (X?) -> DataObservable<Y>?): DataObservable<Y> {
  val result = MediatorDataObservable<Y>()
  result.addSource(this, object : Observer<X> {
    private var source: Observable<Y>? = null

    override fun invoke(value: X?) {
      val newDataObservable = mapFunc(value)
      if (source == newDataObservable) {
        return
      }
      source?.let { result.removeSource(it) }
      source = newDataObservable
      source?.let {
        result.addSource(it) { y -> result.value = y }
      }
    }
  })
  return result
}