package com.chicagoroboto.features.sessions

import com.chicagoroboto.model.Session
import com.chicagoroboto.model.endTime
import com.chicagoroboto.model.startTime
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.Date

@InternalCoroutinesApi
internal actual fun SessionListViewModel.findCurrentSessionIndex(sessions: List<Session>): Int {
  val now = Date()
  return sessions.indexOfFirst { now.after(it.startTime) && now.before(it.endTime) }
}