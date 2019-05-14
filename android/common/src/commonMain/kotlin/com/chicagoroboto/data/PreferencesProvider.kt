package com.chicagoroboto.data

import com.chicagoroboto.ext.randomUUID
import com.chicagoroboto.storage.Settings

class PreferencesProvider(private val settings: Settings) {
    fun getId(): String {
        val id = settings.getString("uid", randomUUID())
        if (!settings.contains("uid")) {
            settings.putString("uid", id)
        }
        return id
    }
}