package com.chicagoroboto.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PreferencesProvider(val app: Application) {

  private val sharedPreferences: SharedPreferences =
      app.getSharedPreferences("devcon", Context.MODE_PRIVATE)

  val uid: String
    get() = sharedPreferences.getString("uid", null)
        ?: UUID.randomUUID().toString().also {
          sharedPreferences.edit().putString("uid", it).apply()
        }
}