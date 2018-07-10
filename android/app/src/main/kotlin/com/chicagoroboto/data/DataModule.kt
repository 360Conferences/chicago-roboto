package com.chicagoroboto.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides @Singleton fun provideFavoriteProvider(sharedPreferences: SharedPreferences): FavoriteProvider {
        return LocalFavoriteProvider(sharedPreferences)
    }

    @Provides @Singleton fun providePreferencesProvider(application: Application): PreferencesProvider {
        return PreferencesProvider(application)
    }

    @Provides @Singleton fun provideNotificationProvider(application: Application): NotificationProvider {
        return LocalNotificationProvider(application)
    }

    @Provides @Singleton fun provideDatabase(@EventId eventId: String): DatabaseReference {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        val eventRef = FirebaseDatabase.getInstance().reference.child("events").child(eventId)
        eventRef.keepSynced(true)
        return eventRef
    }

    @Provides @Singleton fun provideStorage(@EventId eventId: String): StorageReference {
        return FirebaseStorage.getInstance().reference.child(eventId)
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

    @Provides fun provideAvatarProvider(storageReference: StorageReference): AvatarProvider {
        return FirebaseAvatarProvider(storageReference)
    }
}
