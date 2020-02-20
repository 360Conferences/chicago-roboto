package com.chicagoroboto

import android.app.Application
import com.chicagoroboto.injection.AppComponent
import com.chicagoroboto.injection.DaggerAppComponent
import timber.log.LogcatTree
import timber.log.Timber

class DevconApp() : Application() {

  lateinit var component: AppComponent

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(LogcatTree())
    }

    component = DaggerAppComponent.builder()
        .application(this)
        .eventId(BuildConfig.EVENT_ID)
        .build()
  }

  override fun getSystemService(name: String): Any? {
    return when (name) {
      "component" -> component
      else -> super.getSystemService(name)
    }
  }
}
