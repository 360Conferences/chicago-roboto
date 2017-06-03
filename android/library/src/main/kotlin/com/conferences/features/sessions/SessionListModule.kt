package com.conferences.features.sessions

import com.conferences.data.FavoriteProvider
import com.conferences.data.SessionDateProvider
import com.conferences.data.SessionProvider
import com.conferences.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SessionListModule() {

    @Provides fun provideSessionDatePresenter(sessionDateProvider: SessionDateProvider): SessionDateListMvp.Presenter {
        return SessionDatePresenter(sessionDateProvider)
    }

    @Provides fun sessionListPresenter(sessionProvider: SessionProvider,
                                       speakerProvider: SpeakerProvider,
                                       favoriteProvider: FavoriteProvider): SessionListMvp.Presenter {
        return SessionListPresenter(sessionProvider, speakerProvider, favoriteProvider)
    }

}