package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
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

    @Provides fun sessionListViewModel(sessionProvider: SessionProvider,
        speakerProvider: SpeakerProvider,
        favoriteProvider: FavoriteProvider): SessionListViewModel {
        return SessionListViewModel(sessionProvider, speakerProvider, favoriteProvider)
    }

    @Provides fun sessionListPresenter(sessionProvider: SessionProvider,
                                       speakerProvider: SpeakerProvider,
                                       favoriteProvider: FavoriteProvider): SessionListMvp.Presenter {
        return SessionListPresenter(sessionProvider, speakerProvider, favoriteProvider)
    }

}