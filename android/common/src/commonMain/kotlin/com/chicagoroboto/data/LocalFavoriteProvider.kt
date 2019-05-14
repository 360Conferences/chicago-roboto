package com.chicagoroboto.data

import com.chicagoroboto.storage.Settings

class LocalFavoriteProvider(private val settings: Settings) : FavoriteProvider {

  private val favorites: MutableSet<String> = settings
      .getString(KEY_FAVORITES, "")
      .split(SET_DELIMITER)
      .toMutableSet()
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
    storeFavorites(favorites)
    listeners.forEach { it.value.invoke(favorites) }
  }

  override fun removeFavoriteSession(id: String) {
    favorites.remove(id)
    storeFavorites(favorites)
    listeners.forEach { it.value.invoke(favorites) }
  }

  private fun storeFavorites(favorites: Set<String>) {
    settings.putString(KEY_FAVORITES, favorites.joinToString(SET_DELIMITER))
  }

  companion object {
    private const val KEY_FAVORITES = "favorite_sessions"
    private const val SET_DELIMITER = "|"
  }
}