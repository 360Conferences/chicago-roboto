package com.chicagoroboto.features.main

import com.chicagoroboto.features.sessions.SessionNavigator
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import dagger.Module
import dagger.Provides

@Module
class MainModule(val sessionNavigator: SessionNavigator, val speakerNavigator: SpeakerNavigator) {

    @Provides fun sessionNavigator(): SessionNavigator {
        return sessionNavigator
    }

    @Provides fun speakerNavigator(): SpeakerNavigator {
        return speakerNavigator
    }
}