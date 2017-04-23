package com.conferences

import android.app.Application
import com.conferences.data.DataModule
import com.conferences.injection.AppComponent
import com.conferences.injection.AppModule
import com.conferences.injection.DaggerAppComponent

class ConferenceApplication() : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .dataModule(DataModule())
                .appModule(AppModule(this))
                .build()
    }

    override fun getSystemService(name: String?): Any {
        when (name) {
            "component" -> return component
            else -> return super.getSystemService(name)
        }
    }
}