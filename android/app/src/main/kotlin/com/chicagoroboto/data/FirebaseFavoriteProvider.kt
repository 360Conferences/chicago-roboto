package com.chicagoroboto.data

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import timber.log.error
import javax.inject.Inject

class FirebaseFavoriteProvider @Inject constructor(
    private val database: DatabaseReference
) : FavoriteProvider {

  private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()
  private val queries: MutableMap<Any, Query> = mutableMapOf()

  override fun favorites(userId: String): Flow<Set<String>> = channelFlow {
    val query = database.child("users").child(userId).child("favorites")
    val listener = query.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        val favs = if (data.exists()) data.getValue<Map<String, Boolean>>() else null
        channel.offer(favs?.keys ?: emptySet<String>())
      }

      override fun onCancelled(error: DatabaseError) {
        Timber.error(error.toException()) { "Error fetching favorites for user[$userId] from Firebase." }
      }
    })

    awaitClose { query.removeEventListener(listener) }
  }

  override fun addFavoriteListener(userId: String, onComplete: (sessions: Set<String>) -> Unit) {
    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        val typeIndicator = object : GenericTypeIndicator<HashMap<String, Boolean>>() {}
        val favorites = data.getValue(typeIndicator)
            ?.filter { it.value }
            ?.map { it.key }
            ?.toSet()
        onComplete(favorites ?: emptySet())
      }

      override fun onCancelled(p0: DatabaseError) {
        onComplete(emptySet())
      }
    }

    val query = database
        .child("users").child(userId)
        .child("favorites")
    queries[userId] = query
    query.addValueEventListener(listener)
  }

  override fun removeFavoriteListener(userId: String) {
    queries.remove(userId)?.let { query ->
      listeners.remove(userId)?.let { query.removeEventListener(it) }
    }
  }

  override fun addFavoriteSession(userId: String, id: String) {
    database
        .child("users").child(userId)
        .child("favorites").child(id)
        .setValue(true)
  }

  override fun removeFavoriteSession(userId: String, id: String) {
    database
        .child("users").child(userId)
        .child("favorites").child(id)
        .removeValue()
  }
}
