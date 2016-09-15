package com.gdgchicagowest.windycitydevcon.injection

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides fun application(): Application {
        return application
    }

}