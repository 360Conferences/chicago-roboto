package com.conferences.data

interface FavoriteProvider {
  /**
   * Adds a listener to be updated with the user's current list of favorite session ids.
   */
  fun addFavoriteListener(key: String, onComplete: (sessions: Set<String>) -> Unit)

  /**
   * Removes the listener that was added with <code>key</code>
   */
  fun removeFavoriteListener(key: String)

  fun addFavoriteSession(id: String)
  fun removeFavoriteSession(id: String)
}