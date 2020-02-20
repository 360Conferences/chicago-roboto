package com.chicagoroboto.ext

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DispatchQueue {
  private var paused = true
  private var finished = false
  private var isDraining = false

  private val queue = ArrayDeque<Runnable>()

  fun pause() {
    paused = true
  }

  fun resume() {
    if (!paused) {
      return
    }
    check(!finished) {
      "Cannot resume a finished dispatcher"
    }
    paused = false
    drainQueue()
  }

  fun finish() {
    finished = true
    drainQueue()
  }

  fun drainQueue() {
    if (isDraining) {
      // Block re-entrant calls to avoid deep stacks
      return
    }
    try {
      isDraining = true
      while (queue.isNotEmpty()) {
        if (!canRun()) {
          break
        }
        queue.poll()?.run()
      }
    } finally {
      isDraining = false
    }
  }

  private fun canRun() = finished || !paused

  fun runOrEnqueue(runnable: Runnable) {
    with(Dispatchers.Main.immediate) {
      if (isDispatchNeeded(EmptyCoroutineContext)) {
        dispatch(EmptyCoroutineContext, Runnable {
          enqueue(runnable)
        })
      } else {
        enqueue(runnable)
      }
    }
  }

  private fun enqueue(runnable: Runnable) {
    check(queue.offer(runnable)) {
      "cannot enqueue any more runnables"
    }
    drainQueue()
  }
}

class PausingDispatcher : CoroutineDispatcher() {
  val dispatchQueue = DispatchQueue()
  override fun dispatch(context: CoroutineContext, block: Runnable) {
    dispatchQueue.runOrEnqueue(block)
  }

}
