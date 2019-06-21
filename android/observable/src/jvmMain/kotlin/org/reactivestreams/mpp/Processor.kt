package org.reactivestreams.mpp

/**
 * A Processor represents a processing stage—which is both a [Subscriber]
 * and a [Publisher] and obeys the contracts of both.
 *
 * @param <T> the type of element signaled to the [Subscriber]
 * @param <R> the type of element signaled by the [Publisher]
 */
actual typealias Processor<T, R> = org.reactivestreams.Processor<T, R>