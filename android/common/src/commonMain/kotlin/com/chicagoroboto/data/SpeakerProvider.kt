package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.launch

interface SpeakerProvider {

  @ExperimentalCoroutinesApi fun getSpeakers(): Flow<List<Speaker>>

  fun addSpeakerListener(key: Any, onComplete: (speakers: Map<String, Speaker>?) -> Unit)
  fun addSpeakerListener(id: String, onComplete: (speaker: Speaker?) -> Unit)
  fun removeSpeakerListener(key: Any)

  @ExperimentalCoroutinesApi
  @ExperimentalUnsignedTypes
  class Impl(private val db: DatabaseReferenceWrapper) : SpeakerProvider {

    private val handles = mutableMapOf<Any, ULong>()
    private val queries = mutableMapOf<Any, DatabaseReferenceWrapper>()

    override fun getSpeakers(): Flow<List<Speaker>> = callbackFlow {
      val listener = object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          val speakers = data?.getList(Speaker.Factory::fromDataSnapshot)?.filterNotNull()
              ?: emptyList()
          offer(speakers)
        }

        override fun onCancelled(error: Exception) {
          close()
        }
      }
      val query = db.child("speakers")
      val handle = query.addValueEventListener(listener)
      awaitClose { query.removeEventListener(handle) }
    }

    private fun makeSpeakersValueEventListener(onComplete: (Map<String, Speaker>?) -> Unit) =
      object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          val speakers = data
              ?.getList(Speaker.Factory::fromDataSnapshot)
              ?.filterNotNull()
              ?.filter { it.id != null }
              ?.associateBy { it.id!! }
          onComplete(speakers)
        }

        override fun onCancelled(error: Exception) {
          onComplete(null)
        }
      }

    private fun makeSpeakerValueEventListener(onComplete: (Speaker?) -> Unit) =
      object : ValueEventListenerWrapper {
        override fun onDataChange(data: DataSnapshotWrapper?) {
          onComplete(data?.map(Speaker.Factory::fromDataSnapshot))
        }

        override fun onCancelled(error: Exception) {
          onComplete(null)
        }
      }

    override fun addSpeakerListener(key: Any, onComplete: (Map<String, Speaker>?) -> Unit) {
      val listener = makeSpeakersValueEventListener(onComplete)
      val query = db.child("speakers")
      queries[key] = query
      handles[key] = query.addValueEventListener(listener)
    }

    override fun addSpeakerListener(id: String, onComplete: (Speaker?) -> Unit) {
      val listener = makeSpeakerValueEventListener(onComplete)
      val query = db.child("speakers").child(id)
      queries[id] = query
      handles[id] = query.addValueEventListener(listener)
    }

    override fun removeSpeakerListener(key: Any) {
      val query = queries[key]
      val handle = handles[key]
      if (query != null && handle != null) {
        query.removeEventListener(handle)
      }

      queries.remove(key)
      handles.remove(key)
    }
  }
}