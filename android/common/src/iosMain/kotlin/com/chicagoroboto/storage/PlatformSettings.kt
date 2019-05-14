package com.chicagoroboto.storage

import platform.Foundation.NSUserDefaults

class PlatformSettings : Settings {
  private val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults()

  override fun putBoolean(key: String, value: Boolean) {
    delegate.setBool(value, key)
  }

  override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
    if (contains(key)) delegate.boolForKey(key) else defaultValue

  override fun putString(key: String, value: String) {
    delegate.setObject(value, key)
  }

  override fun getString(key: String, defaultValue: String): String =
    delegate.stringForKey(key) ?: defaultValue

  override fun contains(key: String): Boolean = delegate.objectForKey(key) != null

}