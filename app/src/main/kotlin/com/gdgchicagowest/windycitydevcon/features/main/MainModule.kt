package com.gdgchicagowest.windycitydevcon.features.main

import com.gdgchicagowest.windycitydevcon.features.sessions.SessionNavigator
import com.gdgchicagowest.windycitydevcon.features.speakerdetail.SpeakerNavigator
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