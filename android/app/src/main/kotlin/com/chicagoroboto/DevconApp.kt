package com.chicagoroboto

import android.app.Application
import com.chicagoroboto.data.DataModule
import com.chicagoroboto.injection.AppComponent
import com.chicagoroboto.injection.AppModule
import com.chicagoroboto.injection.DaggerAppComponent

class DevconApp() : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .dataModule(DataModule())
                .appModule(AppModule(this))
                .build()
    }

    override fun getSystemService(name: String): Any? {
        when (name) {
            "component" -> return component
            else -> return super.getSystemService(name)
        }
    }
}