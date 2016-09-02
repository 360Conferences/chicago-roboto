package com.gdgchicagowest.windycitydevcon.features.dates

import com.gdgchicagowest.windycitydevcon.data.SessionDateProvider
import dagger.Module
import dagger.Provides

@Module
class SessionDateModule {
    @Provides fun provideSessionDatePresenter(sessionDateProvider: SessionDateProvider): SessionDateListMvp.Presenter {
        return SessionDatePresenter(sessionDateProvider)
    }
}