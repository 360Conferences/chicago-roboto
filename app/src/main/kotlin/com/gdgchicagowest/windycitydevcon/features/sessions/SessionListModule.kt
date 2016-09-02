package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.data.SessionDateProvider
import com.gdgchicagowest.windycitydevcon.data.SessionProvider
import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SessionListModule(val sessionDisplay: SessionDisplay) {

    @Provides fun sessionDisplay(): SessionDisplay {
        return sessionDisplay
    }

    @Provides fun provideSessionDatePresenter(sessionDateProvider: SessionDateProvider): SessionDateListMvp.Presenter {
        return SessionDatePresenter(sessionDateProvider)
    }

    @Provides fun sessionListPresenter(sessionProvider: SessionProvider, speakerProvider: SpeakerProvider): SessionListMvp.Presenter {
        return SessionListPresenter(sessionProvider, speakerProvider)
    }

}