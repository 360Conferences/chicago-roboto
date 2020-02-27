package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import timber.log.error
import java.util.*
import javax.inject.Inject

class FirebaseSpeakerProvider @Inject constructor(
    database: DatabaseReference,
    storage: StorageReference
) : SpeakerProvider {

  private val speakersRef = database.child("speakers")
  private val avatarRef = storage.child("profiles")

  override fun speakers(): Flow<List<Speaker>> = channelFlow {
    val query = speakersRef
    val listener = query.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        if (data.exists()) {
          channel.offer(data.children.map { it.toSpeaker() }.sortedBy { it.name })
        }
      }
      override fun onCancelled(error: DatabaseError) {
        Timber.error(error.toException()) { "Error fetching speaker list from Firebase." }
      }
    })

    awaitClose { query.removeEventListener(listener) }
  }

  override fun speaker(speakerId: String): Flow<Speaker> = channelFlow {
    val query = speakersRef.child(speakerId)
    val listener = query.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        if (data.exists()) {
          channel.offer(data.toSpeaker())
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Timber.error(error.toException()) { "Error fetching speaker from Firebase." }
      }

    })

    awaitClose { query.removeEventListener(listener) }
  }

  override suspend fun avatar(speakerId: String): String {
    val url = avatarRef.child(speakerId).downloadUrl.await()
    return url.toString()
  }
}
