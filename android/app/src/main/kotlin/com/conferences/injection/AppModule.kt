package com.conferences.injection

import android.app.Application
import com.conferences.R
import com.conferences.data.EventId
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides fun application(): Application {
        return application
    }

    @Provides @EventId fun provideEventId(): String {
        return application.getString(R.string.event_id)
    }

}