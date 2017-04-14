package com.chicagoroboto.data

import android.content.SharedPreferences

class LocalFavoriteProvider(private val prefs: SharedPreferences) : FavoriteProvider {

  private val favoritesKey = "favorite_sessions"

  private val favorites: MutableSet<String> = prefs.getStringSet(favoritesKey, mutableSetOf())
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
    prefs.edit().putStringSet(favoritesKey, favorites).apply()
    listeners.forEach { it.value.invoke(favorites) }
  }

  override fun removeFavoriteSession(id: String) {
    favorites.remove(id)
    prefs.edit().putStringSet(favoritesKey, favorites).apply()
    listeners.forEach { it.value.invoke(favorites) }
  }
}