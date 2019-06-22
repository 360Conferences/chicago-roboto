package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionDateProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@Module
object SessionListModule {

    @Provides
    @JvmStatic
    fun provideSessionDateViewModel(dateProvider: SessionDateProvider)
        = SessionDateViewModel(dateProvider)

    @Provides
    @JvmStatic
    fun sessionListViewModel(
        sessionProvider: SessionProvider,
        speakerProvider: SpeakerProvider,
        favoriteProvider: FavoriteProvider
    ) = SessionListViewModel(sessionProvider, speakerProvider, favoriteProvider)
}