package com.gdgchicagowest.windycitydevcon.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PreferencesProvider(val app: Application) {

    val sharedPreferences: SharedPreferences = app.getSharedPreferences("devcon", Context.MODE_PRIVATE)

    fun getId(): String? {
        if (!sharedPreferences.contains("uid")) {
            sharedPreferences.edit().putString("uid", UUID.randomUUID().toString()).apply();
        }
        return sharedPreferences.getString("uid", null)
    }
}