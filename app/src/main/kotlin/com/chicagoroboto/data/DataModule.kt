package com.chicagoroboto.data

import android.app.Application
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton fun providePreferencesProvider(application: Application): PreferencesProvider {
        return PreferencesProvider(application)
    }

    @Provides @Singleton fun provideDatabase(@EventId eventId: String): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("events").child(eventId)
    }

    @Provides @Singleton fun provideSessionDateProvider(db: DatabaseReference): SessionDateProvider {
        return FirebaseSessionDateProvider(db)
    }

    @Provides @Singleton fun provideSessionProvider(db: DatabaseReference): SessionProvider {
        return FirebaseSessionProvider(db)
    }

    @Provides @Singleton fun provideSpeakerProvider(db: DatabaseReference): SpeakerProvider {
        return FirebaseSpeakerProvider(db)
    }

    @Provides @Singleton fun provideVenueProvider(db: DatabaseReference): VenueProvider {
        return FirebaseVenueProvider(db)
    }

    @Provides @Singleton fun provideReviewProvider(db: DatabaseReference, preferencesProvider: PreferencesProvider): FeedbackProvider {
        return FirebaseFeedbackProvider(db, preferencesProvider)
    }
}
