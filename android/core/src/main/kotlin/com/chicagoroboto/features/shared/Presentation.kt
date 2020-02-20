package com.chicagoroboto.features.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <P : Presenter<*, *>> P.startPresentation(dispatcher: CoroutineDispatcher): Presentation<P> {
  val job = GlobalScope.launch(dispatcher) { start() }
  return Presentation(this, job)
}

class Presentation<P : Presenter<*, *>> internal constructor(
    val presenter: P,
    private val job: Job
) {
  fun stop() = job.cancel()
}
