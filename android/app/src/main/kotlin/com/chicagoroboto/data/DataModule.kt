package com.chicagoroboto.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

  @Binds
  abstract fun bindFavoriteProvider(impl: FirebaseFavoriteProvider): FavoriteProvider

  @Binds
  abstract fun bindNotificationProvider(impl: LocalNotificationProvider): NotificationProvider

  @Binds
  @Singleton
  abstract fun bindSessionDateProvider(impl: FirebaseSessionDateProvider): SessionDateProvider

  @Binds
  @Singleton
  abstract fun bindSessionProvider(impl: FirebaseSessionProvider): SessionProvider

  @Binds
  @Singleton
  abstract fun bindSpeakerProvider(impl: FirebaseSpeakerProvider): SpeakerProvider

  @Binds
  @Singleton
  abstract fun bindVenueProvider(impl: FirebaseVenueProvider): VenueProvider

  @Binds
  @Singleton
  abstract fun bindReviewProvider(impl: FirebaseFeedbackProvider): FeedbackProvider

  @Binds
  abstract fun bindAvatarProvider(impl: FirebaseAvatarProvider): AvatarProvider

  @Binds
  abstract fun bindUserProvider(impl: FirebaseUserProvider): UserProvider

  companion object {

    @Provides
    @Singleton
    fun provideDatabase(@EventId eventId: String): DatabaseReference {
      FirebaseDatabase.getInstance().setPersistenceEnabled(true);
      val eventRef = FirebaseDatabase.getInstance().reference.child("events").child(eventId)
      eventRef.keepSynced(true)
      return eventRef
    }

    @Provides
    @Singleton
    fun provideStorage(@EventId eventId: String): StorageReference =
        FirebaseStorage.getInstance().reference.child(eventId)

    @Provides
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()
  }
}
