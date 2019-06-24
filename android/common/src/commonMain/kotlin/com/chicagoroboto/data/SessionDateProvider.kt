package com.chicagoroboto.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface SessionDateProvider {

  @ExperimentalCoroutinesApi fun getSessionDates(): Flow<List<String>>

  fun addSessionDateListener(key: Any, onComplete: (sessionDates: List<String>?) -> Unit)
  fun removeSessionDateListener(key: Any)

  @ExperimentalUnsignedTypes
  class Impl(db: DatabaseReferenceWrapper) : SessionDateProvider {

    private val ref = db.child("config").child("event_dates")

    @ExperimentalCoroutinesApi override fun getSessionDates(): Flow<List<String>> = callbackFlow {
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          val dates = data?.getList { it.getValue() as? String }?.filterNotNull() ?: emptyList()
          offer(dates)
        }

        override fun onCancelled(error: Exception) {
          close()
        }
      }
      val handle = ref.addValueEventListener(listener)
      awaitClose { ref.removeEventListener(handle) }
    }

    private val handles = mutableMapOf<Any, ULong>()

    override fun addSessionDateListener(key: Any, onComplete: (List<String>?) -> Unit) {
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          onComplete(data?.getList { it.getValue() as? String }?.filterNotNull())
        }

        override fun onCancelled(error: Exception) {
          onComplete(null)
        }
      }
      handles[key] = ref.addValueEventListener(listener)
    }

    override fun removeSessionDateListener(key: Any) {
      handles[key]?.also { ref.removeEventListener(it) }
    }
  }
}