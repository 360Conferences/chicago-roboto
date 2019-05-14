package com.chicagoroboto.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chicagoroboto.storage.PlatformSettings
import com.chicagoroboto.storage.Settings
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@ExperimentalUnsignedTypes
@Module
class DataModule {

    @Provides @Singleton fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    @Provides @Singleton fun provideSettings(sharedPrefs: SharedPreferences): Settings =
        PlatformSettings(sharedPrefs)

    @Provides @Singleton fun provideFavoriteProvider(settings: Settings): FavoriteProvider =
        LocalFavoriteProvider(settings)

    @Provides @Singleton fun providePreferencesProvider(settings: Settings): PreferencesProvider =
        PreferencesProvider(settings)

    @Provides @Singleton fun provideNotificationProvider(application: Application): NotificationProvider =
        LocalNotificationProvider(application)

    @Provides @Singleton fun provideDatabase(@EventId eventId: String): DatabaseReferenceWrapper {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        val eventRef = FirebaseDatabase.getInstance().reference.child("events").child(eventId)
        eventRef.keepSynced(true)
        return FirebaseDatabaseReference(eventRef)
    }

    @Provides @Singleton fun provideStorage(@EventId eventId: String): StorageReference {
        return FirebaseStorage.getInstance().reference.child(eventId)
    }

    @Provides @Singleton fun provideSessionDateProvider(db: DatabaseReferenceWrapper): SessionDateProvider {
        return SessionDateProvider.Impl(db)
    }

    @Provides @Singleton fun provideSessionProvider(db: DatabaseReferenceWrapper): SessionProvider {
        return SessionProvider.Impl(db)
    }

    @Provides @Singleton fun provideSpeakerProvider(db: DatabaseReferenceWrapper): SpeakerProvider {
        return SpeakerProvider.Impl(db)
    }

    @Provides @Singleton fun provideVenueProvider(db: DatabaseReferenceWrapper): VenueProvider {
        return VenueProvider.Impl(db)
    }

    @Provides @Singleton fun provideReviewProvider(db: DatabaseReferenceWrapper, preferencesProvider: PreferencesProvider): FeedbackProvider {
        return FeedbackProvider.Impl(db, preferencesProvider)
    }

    @Provides fun provideAvatarProvider(storageReference: StorageReference): AvatarProvider {
        return FirebaseAvatarProvider(storageReference)
    }
}
