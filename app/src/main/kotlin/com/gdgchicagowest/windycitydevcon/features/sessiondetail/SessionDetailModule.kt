package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import com.gdgchicagowest.windycitydevcon.data.SessionProvider
import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SessionDetailModule {
    @Provides fun sessionDetailPresenter(sessionProvider: SessionProvider, speakerProvider: SpeakerProvider): SessionDetailMvp.Presenter {
        return SessionDetailPresenter(sessionProvider, speakerProvider)
    }
}