package com.chicagoroboto.data

import com.chicagoroboto.ext.addValueEventListener
import com.chicagoroboto.model.Speaker
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class FirebaseSpeakerProvider @Inject constructor(
    database: DatabaseReference,
    storage: StorageReference
) : SpeakerProvider {

  private val speakersRef = database.child("speakers")
  private val avatarRef = storage.child("profiles")

  private val queries: MutableMap<Any, Query> = mutableMapOf()
  private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()

  override fun speaker(speakerId: String): Flow<Speaker> = channelFlow {
    val query = speakersRef.child(speakerId)
    val listener = query.addValueEventListener(
        onDataChange = {
          if (it.exists()) {
            it.getValue<Speaker>()?.let(channel::offer)
          }
        }
    )
    awaitClose { query.removeEventListener(listener) }
  }

  override suspend fun avatar(speakerId: String): String {
    val url = avatarRef.child(speakerId).downloadUrl.await()
    return url.toString()
  }

  override fun addSpeakerListener(key: Any, onComplete: (Map<String, Speaker>?) -> Unit) {
    if (queries[key] != null) {
      removeSpeakerListener(key)
    }

    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        val typeIndicator = object : GenericTypeIndicator<HashMap<String, Speaker>>() {}
        onComplete(data.getValue(typeIndicator))
      }

      override fun onCancelled(e: DatabaseError) {
        onComplete(null)
      }
    }
    listeners[key] = listener

    val query = speakersRef
    query.addValueEventListener(listener)
    queries[key] = query
  }

  override fun addSpeakerListener(id: String, onComplete: (Speaker?) -> Unit) {
    if (queries[id] != null) {
      removeSpeakerListener(id)
    }

    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        onComplete(data.getValue(Speaker::class.java))
      }

      override fun onCancelled(e: DatabaseError) {
        onComplete(null)
      }
    }
    listeners[id] = listener

    val query = speakersRef.child(id)
    query.addValueEventListener(listener)
    queries[id] = query
  }

  override fun removeSpeakerListener(key: Any) {
    queries.remove(key)?.let { query ->
      listeners.remove(key)?.let { query.removeEventListener(it) }
    }
  }
}
