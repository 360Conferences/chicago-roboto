package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.SessionDateProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SessionListModule() {

    @Provides fun provideSessionDatePresenter(sessionDateProvider: SessionDateProvider): SessionDateListMvp.Presenter {
        return SessionDatePresenter(sessionDateProvider)
    }

    @Provides fun sessionListPresenter(sessionProvider: SessionProvider, speakerProvider: SpeakerProvider): SessionListMvp.Presenter {
        return SessionListPresenter(sessionProvider, speakerProvider)
    }

}