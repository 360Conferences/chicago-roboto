package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
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