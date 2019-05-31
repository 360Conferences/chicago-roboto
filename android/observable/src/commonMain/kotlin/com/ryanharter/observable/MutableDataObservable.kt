package com.ryanharter.observable

open class MutableDataObservable<T> : DataObservable<T>() {
  override var value
    get() = super.value
    public set(value) {
      super.value = value
    }
}