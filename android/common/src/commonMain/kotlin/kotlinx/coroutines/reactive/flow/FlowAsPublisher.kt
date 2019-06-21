/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines.reactive.flow

import com.chicagoroboto.reactive.*
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*
import kotlin.jvm.JvmName
import kotlin.jvm.Volatile

/**
 * Transforms the given flow to a spec-compliant [Publisher].
 */
@JvmName("from")
@ExperimentalCoroutinesApi
public fun <T : Any> Flow<T>.asPublisher(): Publisher<T> = FlowAsPublisher(this)

/**
 * Adapter that transforms [Flow] into TCK-complaint [Publisher].
 * [cancel] invocation cancels the original flow.
 */
@ExperimentalCoroutinesApi
@Suppress("PublisherImplementation")
private class FlowAsPublisher<T : Any>(private val flow: Flow<T>) : Publisher<T> {

  override fun subscribe(s: Subscriber<in T>) {
    s.onSubscribe(FlowSubscription(flow, s))
  }

  private class FlowSubscription<T>(val flow: Flow<T>, val subscriber: Subscriber<in T>) :
      Subscription {
    @Volatile
    internal var canceled: Boolean = false
    private val requested = atomic(0L)
    private val producer: AtomicRef<CancellableContinuation<Unit>?> = atomic(null)

    // This is actually optimizable
    @InternalCoroutinesApi
    private var job = GlobalScope.launch(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
      try {
        consumeFlow()
        subscriber.onComplete()
      } catch (e: Throwable) {
        // Failed with real exception, not due to cancellation
        if (!coroutineContext[Job]!!.isCancelled) {
          subscriber.onError(e)
        }
      }
    }

    @InternalCoroutinesApi
    private suspend fun consumeFlow() {
      flow.collect { value ->
        if (!coroutineContext.isActive) {
          subscriber.onComplete()
          coroutineContext.ensureActive()
        }

        if (requested.value == 0L) {
          suspendCancellableCoroutine<Unit> {
            producer.value = it
            if (requested.value != 0L) it.resumeSafely()
          }
        }

        requested.decrementAndGet()
        subscriber.onNext(value)
      }
    }

    @InternalCoroutinesApi
    override fun cancel() {
      canceled = true
      job.cancel()
    }

    @InternalCoroutinesApi
    override fun request(n: Long) {
      if (n <= 0) {
        return
      }

      if (canceled) return

      job.start()
      var snapshot: Long
      var newValue: Long
      do {
        snapshot = requested.value
        newValue = snapshot + n
        if (newValue <= 0L) newValue = Long.MAX_VALUE
      } while (!requested.compareAndSet(snapshot, newValue))

      val prev = producer.value
      if (prev == null || !producer.compareAndSet(prev, null)) return
      prev.resumeSafely()
    }

    @InternalCoroutinesApi
    private fun CancellableContinuation<Unit>.resumeSafely() {
      val token = tryResume(Unit)
      if (token != null) {
        completeResume(token)
      }
    }
  }
}