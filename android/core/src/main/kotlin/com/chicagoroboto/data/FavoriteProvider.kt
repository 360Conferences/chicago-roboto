package com.chicagoroboto.data

import kotlinx.coroutines.flow.Flow

interface FavoriteProvider {

  fun favorites(userId: String): Flow<Set<String>>

  /**
   * Adds a listener to be updated with the user's current list of favorite session ids.
   */
  fun addFavoriteListener(userId: String, onComplete: (sessions: Set<String>) -> Unit)

  /**
   * Removes the listener that was added with <code>key</code>
   */
  fun removeFavoriteListener(userId: String)

  fun addFavoriteSession(userId: String, id: String)
  fun removeFavoriteSession(userId: String, id: String)
}
