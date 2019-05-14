package com.chicagoroboto.storage

import android.content.SharedPreferences

class PlatformSettings(private val prefs: SharedPreferences) : Settings {

  override fun putBoolean(key: String, value: Boolean) {
    prefs.edit().putBoolean(key, value).apply()
  }

  override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
    prefs.getBoolean(key, defaultValue)

  override fun putString(key: String, value: String) {
    prefs.edit().putString(key, value).apply()
  }

  override fun getString(key: String, defaultValue: String): String =
    prefs.getString(key, defaultValue)

  override fun contains(key: String): Boolean = prefs.contains(key)

}