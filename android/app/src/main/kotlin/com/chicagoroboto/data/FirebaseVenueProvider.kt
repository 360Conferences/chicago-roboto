package com.chicagoroboto.data

import com.chicagoroboto.model.Venue
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import timber.log.error
import javax.inject.Inject

class FirebaseVenueProvider @Inject constructor(
    val database: DatabaseReference
) : VenueProvider {

  override fun venue(): Flow<Venue> = channelFlow {
    val query = database.child("venue")
    val listener = query.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        offer(data.toVenue())
      }

      override fun onCancelled(error: DatabaseError) {
        // no op
      }
    })

    awaitClose { query.removeEventListener(listener) }
  }
}
