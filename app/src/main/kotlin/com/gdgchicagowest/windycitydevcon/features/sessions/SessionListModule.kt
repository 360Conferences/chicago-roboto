package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.data.SessionProvider
import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SessionListModule {

    @Provides fun sessionListPresenter(sessionProvider: SessionProvider, speakerProvider: SpeakerProvider): SessionListMvp.Presenter {
        return SessionListPresenter(sessionProvider, speakerProvider)
    }

}