package com.chicagoroboto.features.sessions

import com.chicagoroboto.model.Session
import com.chicagoroboto.model.endTime
import com.chicagoroboto.model.startTime
import kotlinx.coroutines.InternalCoroutinesApi
import platform.Foundation.NSDate
import platform.Foundation.NSOrderedAscending
import platform.Foundation.NSOrderedDescending
import platform.Foundation.compare

@InternalCoroutinesApi
internal actual fun SessionListViewModel.findCurrentSessionIndex(sessions: List<Session>): Int {
  val now = NSDate()
  return sessions.indexOfFirst {
    it.startTime?.compare(now) == NSOrderedAscending &&
        it.endTime?.compare(now) == NSOrderedDescending
  }
}