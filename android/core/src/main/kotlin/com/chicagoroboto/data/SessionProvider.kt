package com.chicagoroboto.data

import com.chicagoroboto.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionProvider {
  fun session(sessionId: String): Flow<Session>
  fun sessionsByDate(date: String): Flow<List<Session>>
  fun addSessionListener(id: String, onComplete: (Session?) -> Unit)
  fun addSessionListener(key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit)
  fun removeSessionListener(key: Any)
}
