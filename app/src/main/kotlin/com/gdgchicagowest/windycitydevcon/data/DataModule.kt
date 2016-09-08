package com.gdgchicagowest.windycitydevcon.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton fun provideSessionDateProvider(): SessionDateProvider {
        return FirebaseSessionDateProvider()
    }

    @Provides @Singleton fun provideSessionProvider(): SessionProvider {
        return FirebaseSessionProvider()
    }

    @Provides @Singleton fun provideSpeakerProvider(): SpeakerProvider {
        return FirebaseSpeakerProvider()
    }

    @Provides @Singleton fun provideVenueProvider(): VenueProvider {
        return FirebaseVenueProvider()
    }
}