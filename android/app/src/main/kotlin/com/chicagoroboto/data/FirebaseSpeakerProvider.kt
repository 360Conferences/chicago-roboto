package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.HashMap
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseSpeakerProvider(private val database: DatabaseReference,
                              private val avatarProvider: AvatarProvider) : SpeakerProvider {

  private val queries: MutableMap<Any, Query> = mutableMapOf()
  private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()

  override suspend fun getSpeakersMap(key: Any) : Map<String, Speaker> {
    if (queries[key] != null) {
      removeSpeakerListener(key)
    }
    val speakersWithoutUrl = getSpeakers(key)
    return populateAvatarUrls(speakersWithoutUrl)
  }

  private suspend fun getSpeakers(key: Any) = suspendCoroutine<Map<String, Speaker>> {
    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot?) {
        val typeIndicator = object : GenericTypeIndicator<HashMap<String, Speaker>>() {}
        val speakersWithoutUrl = data?.getValue(typeIndicator)!!
        it.resume(speakersWithoutUrl)
      }

      override fun onCancelled(e: DatabaseError) {
        it.resumeWithException(e.toException())
      }
    }
    listeners[key] = listener

    val query = database.child("speakers")
    query.addValueEventListener(listener)
    queries[key] = query

  }

  private suspend fun populateAvatarUrls(speakerMap: Map<String, Speaker>): Map<String, Speaker> {
    val speakerList = speakerMap.values
    return speakerList.map { speaker ->
      speaker to async {
        avatarProvider.getAvatarUri(speaker)
      }
    }.map { (s, deferred) ->
      val avatarUrl = deferred.await()
      Speaker(s.id, s.name, s.title, s.company, s.email, s.twitter, s.github, s.bio, avatarUrl)
    }.associateBy { it.id!! }
  }

  override suspend fun getSpeaker(id: String): Speaker {
    if (queries[id] != null) {
      removeSpeakerListener(id)
    }

    val s = getSpeakerFromFirebase(id)
    val avatarUri = avatarProvider.getAvatarUri(s)
    return Speaker(s.id, s.name, s.title, s.company, s.email, s.twitter, s.github, s.bio, avatarUri)
  }

  private suspend fun getSpeakerFromFirebase(id: String) = suspendCoroutine<Speaker> { c ->
    addSpeakerListener(id, {
      if (it != null) {
        c.resume(it)
      }
      else {
        c.resumeWithException(IllegalStateException("No Speaker"))
      }
    })
  }

  override fun addSpeakerListener(id: String, onComplete: (Speaker?) -> Unit) {
    if (queries[id] != null) {
      removeSpeakerListener(id)
    }

    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot?) {
        onComplete(data?.getValue(Speaker::class.java))
      }

      override fun onCancelled(e: DatabaseError?) {
        onComplete(null)
      }
    }
    listeners[id] = listener

    val query = database.child("speakers").child(id)
    query.addValueEventListener(listener)
    queries[id] = query
  }

  override fun removeSpeakerListener(key: Any) {
    val query = queries[key]
    query?.removeEventListener(listeners[key])

    queries.remove(key)
    listeners.remove(key)
  }
}