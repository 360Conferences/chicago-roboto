package com.chicagoroboto.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.util.*
import javax.inject.Inject

class PreferencesProvider @Inject constructor(
    val app: Application
) {

  val sharedPreferences: SharedPreferences =
      app.getSharedPreferences("devcon", Context.MODE_PRIVATE)

  val uid: String
    get() = sharedPreferences.getString("uid", null)
        ?: UUID.randomUUID().toString().also {
          sharedPreferences.edit().putString("uid", it).apply()
        }

  fun getBoolean(name: String, default: Boolean = false): Boolean = sharedPreferences.getBoolean(name, default)
  fun putBoolean(name: String, value: Boolean) = sharedPreferences.edit().apply {
    putBoolean(name, value)
    apply()
  }

  fun getStringSet(name: String, default: Set<String> = emptySet()): Set<String> =
      sharedPreferences.getStringSet(name, null) ?: default
  fun putStringSet(name: String, value: Set<String>) = sharedPreferences.edit().apply {
    putStringSet(name, value)
    apply()
  }
}
