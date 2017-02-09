package com.chicagoroboto.injection

import android.app.Application
import com.chicagoroboto.BuildConfig
import com.chicagoroboto.data.EventId
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides fun application(): Application {
        return application
    }

    @Provides @EventId fun provideEventId(): String {
        return BuildConfig.EVENT_ID
    }

}