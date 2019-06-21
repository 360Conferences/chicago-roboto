package com.chicagoroboto.reactive

/**
 * Will receive call to [.onSubscribe] once after passing an instance of [Subscriber] to
 * [Publisher.subscribe].
 *
 * No further notifications will be received until [Subscription.request] is called.
 *
 * After signaling demand:
 *
 *  * One or more invocations of [.onNext] up to the maximum number defined by
 *    [Subscription.request]
 *  * Single invocation of [.onError] or [Subscriber.onComplete] which signals a terminal state
 *    after which no further events will be sent.
 *
 * Demand can be signaled via [Subscription.request] whenever the [Subscriber] instance is capable
 * of handling more.
 *
 * @param <T> the type of element signaled.
 */
expect interface Subscriber<T> {
  /**
   * Invoked after calling [Publisher.subscribe].
   *
   * No data will start flowing until [Subscription.request] is invoked.
   *
   * It is the responsibility of this [Subscriber] instance to call [Subscription.request] whenever
   * more data is wanted.
   *
   * The [Publisher] will send notifications only in response to [Subscription.request].
   *
   * @param s [Subscription] that allows requesting data via [Subscription.request]
   */
  fun onSubscribe(s: Subscription)

  /**
   * Data notification sent by the [Publisher] in response to requests to [Subscription.request].
   *
   * @param t the element signaled
   */
  fun onNext(t: T)

  /**
   * Failed terminal state.
   *
   *
   * No further events will be sent even if [Subscription.request] is invoked again.
   *
   * @param t the throwable signaled
   */
  fun onError(t: Throwable)

  /**
   * Successful terminal state.
   *
   *
   * No further events will be sent even if [Subscription.request] is invoked again.
   */
  fun onComplete()
}