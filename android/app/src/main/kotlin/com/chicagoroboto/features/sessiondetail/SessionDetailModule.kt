package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.NotificationProvider
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

    @Provides fun sessionDetailPresenter(sessionProvider: SessionProvider,
                                         speakerProvider: SpeakerProvider,
                                         favoriteProvider: FavoriteProvider,
                                         notificationProvider: NotificationProvider): SessionDetailMvp.Presenter {
        return SessionDetailPresenter(sessionProvider, speakerProvider, favoriteProvider, notificationProvider)
    }
}