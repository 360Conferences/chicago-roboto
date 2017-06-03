package com.conferences.features.sessiondetail

import com.conferences.data.FavoriteProvider
import com.conferences.data.NotificationProvider
import com.conferences.data.SessionProvider
import com.conferences.data.SpeakerProvider
import com.conferences.features.speakerdetail.SpeakerNavigator
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