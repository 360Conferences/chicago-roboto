package com.ryanharter.observable

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DataObservableTest {

  private lateinit var observable: FakeDataObservable<String>

  @BeforeTest fun setup() {
    observable = FakeDataObservable()
  }

  @Test fun `toggles active state when observed`() {
    val observer = { p1: String -> }
    observable.observe(observer)
    assertFalse(observable.onInactiveCalled)
    assertTrue(observable.onActiveCalled)
    assertTrue(observable.isLive)

    observable.removeObserver(observer)
    assertTrue(observable.onInactiveCalled)
  }

  class FakeDataObservable<T> : DataObservable<T>() {

    var onActiveCalled = false
    override fun onActive() {
      onActiveCalled = true
    }

    var onInactiveCalled = false
    override fun onInactive() {
      onInactiveCalled = true
    }
  }
}