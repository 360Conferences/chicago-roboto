package com.chicagoroboto.data

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LocalFavoriteProvider @Inject constructor(
    private val prefs: PreferencesProvider
) : FavoriteProvider {

  private val favoritesKey = "favorite_sessions"

  private val _favorites = ConflatedBroadcastChannel(prefs.getStringSet(favoritesKey))
  private val listeners: MutableMap<String, ((sessions: Set<String>) -> Unit)> = mutableMapOf()

  override fun favorites(userId: String): Flow<Set<String>> = _favorites.asFlow()

  override fun addFavoriteListener(key: String, onComplete: (sessions: Set<String>) -> Unit) {
    listeners[key] = onComplete
    _favorites.valueOrNull?.let { onComplete(it) }
  }

  override fun removeFavoriteListener(key: String) {
    listeners.remove(key)
  }

  override fun addFavoriteSession(userId: String, id: String) {
    val favs = _favorites.valueOrNull?.toMutableSet() ?: mutableSetOf()
    favs.add(id)
    _favorites.offer(favs)
    listeners.values.forEach { it(favs) }
    prefs.putStringSet(favoritesKey, favs)
  }

  override fun removeFavoriteSession(userId: String, id: String) {
    val favs = _favorites.valueOrNull?.toMutableSet() ?: return
    if (favs.remove(id)) {
      _favorites.offer(favs)
      listeners.values.forEach { it(favs) }
      prefs.putStringSet(favoritesKey, favs)
    }
  }
}
