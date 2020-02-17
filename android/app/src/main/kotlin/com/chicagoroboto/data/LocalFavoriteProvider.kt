package com.chicagoroboto.data

import javax.inject.Inject

class LocalFavoriteProvider @Inject constructor(
    private val prefs: PreferencesProvider
) : FavoriteProvider {

  private val favoritesKey = "favorite_sessions"

  private val favorites: MutableSet<String> = prefs.getStringSet(favoritesKey).toMutableSet()
  private val listeners: MutableMap<String, ((sessions: Set<String>) -> Unit)> = mutableMapOf()

  override fun addFavoriteListener(key: String, onComplete: (sessions: Set<String>) -> Unit) {
    listeners[key] = onComplete
    onComplete(favorites)
  }

  override fun removeFavoriteListener(key: String) {
    listeners.remove(key)
  }

  override fun addFavoriteSession(id: String) {
    favorites.add(id)
    prefs.putStringSet(favoritesKey, favorites)
    listeners.forEach { it.value.invoke(favorites) }
  }

  override fun removeFavoriteSession(id: String) {
    favorites.remove(id)
    prefs.putStringSet(favoritesKey, favorites)
    listeners.forEach { it.value.invoke(favorites) }
  }
}
