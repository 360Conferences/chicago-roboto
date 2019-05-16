package com.chicagoroboto.data

import com.chicagoroboto.model.Session

interface SessionProvider {
  fun addSessionListener(id: String, onComplete: (Session?) -> Unit)
  fun addSessionListener(key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit)
  fun removeSessionListener(key: Any)

  @ExperimentalUnsignedTypes
  class Impl(private val db: DatabaseReferenceWrapper) : SessionProvider {

    private val handles: MutableMap<Any, ULong> = mutableMapOf()
    private val queries: MutableMap<Any, DatabaseReferenceWrapper> = mutableMapOf()

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