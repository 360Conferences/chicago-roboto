package com.chicagoroboto.data

import com.chicagoroboto.model.Session
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import timber.log.error
import javax.inject.Inject

class FirebaseSessionProvider @Inject constructor(
    database: DatabaseReference
) : SessionProvider {

  private val sessionsRef = database.child("sessions")
  private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()
  private val queries: MutableMap<Any, Query> = mutableMapOf()

  override fun session(sessionId: String): Flow<Session> = channelFlow {
    val query = sessionsRef.child(sessionId)
    val listener = query.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        if (data.exists()) {
          channel.offer(data.toSession())
        }
      }

      override fun onCancelled(e: DatabaseError) {
        Timber.error(e.toException()) { "Failed to get session[$sessionId] from Firebase."}
      }
    })

    awaitClose { query.removeEventListener(listener) }
  }

  override fun sessionsByDate(date: String): Flow<List<Session>> = channelFlow {
    val listener = sessionsRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        if (data.exists()) {
          val sessions = data.children.map { it.toSession() }
              .filter { it.start_time.startsWith(date) }
              .sortedBy { it.start_time }
          channel.offer(sessions)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Timber.error(error.toException()) { "Failed to get sessions by date: $date" }
      }
    })

    awaitClose { sessionsRef.removeEventListener(listener) }
  }

  override fun addSessionListener(id: String, onComplete: (Session?) -> Unit) {
    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        onComplete(data.getValue(Session::class.java))
      }

      override fun onCancelled(e: DatabaseError) {
        onComplete(null)
      }
    }
    listeners[id] = listener

    val query = sessionsRef.child(id)
    queries[id] = query
    query.addValueEventListener(listener)
  }

  override fun addSessionListener(key: Any, date: String, onComplete: (List<Session>?) -> Unit) {
    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        val typeIndicator = object : GenericTypeIndicator<HashMap<String, @JvmSuppressWildcards Session>>() {}
        val sessions = data.getValue(typeIndicator)
            ?.map { it.value }
            ?.filter { it.start_time?.startsWith(date) ?: false }
            ?.sortedBy { it.startTime }
        onComplete(sessions)
      }

      override fun onCancelled(e: DatabaseError) {
        onComplete(null)
      }
    }
    listeners[key] = listener

    val query = sessionsRef
    queries[key] = query
    query.addValueEventListener(listener)
  }

  override fun removeSessionListener(key: Any) {
    queries.remove(key)?.let { query ->
      listeners.remove(key)?.let { query.removeEventListener(it) }
    }
  }
}
