package com.gdgchicagowest.windycitydevcon

import android.app.Application
import com.gdgchicagowest.windycitydevcon.data.DataModule
import com.gdgchicagowest.windycitydevcon.injection.AppComponent
import com.gdgchicagowest.windycitydevcon.injection.AppModule
import com.gdgchicagowest.windycitydevcon.injection.DaggerAppComponent

class DevconApp() : Application() {

    companion object {
        @JvmStatic lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .dataModule(DataModule())
                .appModule(AppModule(this))
                .build()
    }
}