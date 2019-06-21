package com.chicagoroboto.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface FavoriteProvider {

  @ExperimentalCoroutinesApi
  fun getFavorites(): Flow<Set<String>>

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