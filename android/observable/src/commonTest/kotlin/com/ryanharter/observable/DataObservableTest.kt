package com.ryanharter.observable

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DataObservableTest {

  private lateinit var observable: FakeDataObservable<String>
  private lateinit var observer: TestObserver<String>

  @BeforeTest fun setup() {
    observable = FakeDataObservable()
    observer = TestObserver()
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


  @Test
  fun `returns no data before value is set`() {
    observable.observe(observer)

    val data = observer.lastValue
    assertNull(data)
  }

  @Test
  fun `observer can set value`() {
    var inObserver = false
    val observer2 = TestObserver<String> {
      assertFalse(inObserver)
      inObserver = true
      if (it == "outer") {
        observable.value = "inner"
      }
      inObserver = false
    }
    observable.observe(observer)
    observable.observe(observer2)
    observable.value = "outer"
    assertEquals(2, observer.values.size)
    assertEquals("inner", observer.lastValue)
    assertEquals(2, observer2.values.size)
    assertEquals("inner", observer2.lastValue)
  }

  @Test
  fun `observer can remove itself`() {
    val observer2 = TestObserver<String>()
    observer2.onInvoke = {
      observable.removeObserver(observer2)
    }
    observable.observe(observer2)
    observable.observe(observer)
    observable.value = "value"
    assertEquals("value", observer.lastValue)
  }

  class TestObserver<T>(var onInvoke: ((T) -> Unit)? = null) : Observer<T> {

    val values = mutableListOf<T>()
    val lastValue: T?
      get() = values.lastOrNull()

    override fun invoke(value: T) {
      values.add(value)
      onInvoke?.invoke(value)
    }
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