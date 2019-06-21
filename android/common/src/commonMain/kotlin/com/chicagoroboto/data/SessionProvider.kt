package com.chicagoroboto.data

import com.chicagoroboto.model.Session
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.launch

interface SessionProvider {

  fun sessionsForDate(date: String): Flow<List<Session>>

  fun addSessionListener(id: String, onComplete: (Session?) -> Unit)
  fun addSessionListener(key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit)
  fun removeSessionListener(key: Any)

  @ExperimentalCoroutinesApi
  @FlowPreview
  @ExperimentalUnsignedTypes
  class Impl(private val db: DatabaseReferenceWrapper) : SessionProvider {

    private val handles: MutableMap<Any, ULong> = mutableMapOf()
    private val queries: MutableMap<Any, DatabaseReferenceWrapper> = mutableMapOf()

    override fun sessionsForDate(date: String): Flow<List<Session>> = callbackFlow {
      val query = db.child("sessions")
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          val sessions = data
              ?.getList(Session.Factory::fromDataSnapshot)
              ?.filterNotNull()
              ?.filter { it.start_time?.startsWith(date) ?: false }
              ?.sortedBy { it.start_time }
              ?.toList() ?: emptyList()
          offer(sessions)
        }
        override fun onCancelled(error: Exception) {
          close()
        }
      }
      val handle = query.addValueEventListener(listener)
      awaitClose { query.removeEventListener(handle) }
    }

    override fun addSessionListener(id: String, onComplete: (Session?) -> Unit) {
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          onComplete(data?.map(Session.Factory::fromDataSnapshot))
        }

        override fun onCancelled(error: Exception) {
          onComplete(null)
        }
      }

      val query = db["sessions"].child(id)
      queries[id] = query
      handles[id] = query.addValueEventListener(listener)
    }

    override fun addSessionListener(
        key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit
    ) {
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          val sessions = data
              ?.getList(Session.Factory::fromDataSnapshot)
              ?.filterNotNull()
              ?.filter { it.start_time?.startsWith(date) ?: false }
              ?.sortedBy { it.start_time }
              ?.toList()

          onComplete(sessions)
        }

        override fun onCancelled(error: Exception) {
          onComplete(null)
        }
      }

      val query = db.child("sessions")
      queries[key] = query
      handles[key] = query.addValueEventListener(listener)
    }

    override fun removeSessionListener(key: Any) {
      val query = queries.remove(key)
      val handle = handles.remove(key)
      if (query != null && handle != null) {
        query.removeEventListener(handle)
      }
    }
  }
}