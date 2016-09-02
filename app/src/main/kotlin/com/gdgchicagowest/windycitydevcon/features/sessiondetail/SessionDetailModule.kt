package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import com.gdgchicagowest.windycitydevcon.data.SessionProvider
import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import com.gdgchicagowest.windycitydevcon.features.speakerdetail.SpeakerNavigator
import dagger.Module
import dagger.Provides

@Module
class SessionDetailModule(private val speakerNavigator: SpeakerNavigator) {

    @Provides fun speakerNavigator(): SpeakerNavigator {
        return speakerNavigator
    }

    @Provides fun sessionDetailPresenter(sessionProvider: SessionProvider, speakerProvider: SpeakerProvider): SessionDetailMvp.Presenter {
        return SessionDetailPresenter(sessionProvider, speakerProvider)
    }
}