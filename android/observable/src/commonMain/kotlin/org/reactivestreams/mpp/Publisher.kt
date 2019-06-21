package org.reactivestreams.mpp

/**
 * A [Publisher] is a provider of a potentially unbounded number of sequenced elements, publishing
 * them according to the demand received from its [Subscriber](s).
 *
 * A [Publisher] can serve multiple [Subscriber]s subscribed [.subscribe] dynamically
 * at various points in time.
 *
 * @param <T> the type of element signaled.
 */
expect interface Publisher<T> {

  /**
   * Request [Publisher] to start streaming data.
   *
   *
   * This is a "factory method" and can be called multiple times, each time starting a new
   * [Subscription].
   *
   *
   * Each [Subscription] will work for only a single [Subscriber].
   *
   *
   * A [Subscriber] should only subscribe once to a single [Publisher].
   *
   *
   * If the [Publisher] rejects the subscription attempt or otherwise fails it will
   * signal the error via [Subscriber.onError].
   *
   * @param s the [Subscriber] that will consume signals from this [Publisher]
   */
  fun subscribe(s: Subscriber<in T>)
}