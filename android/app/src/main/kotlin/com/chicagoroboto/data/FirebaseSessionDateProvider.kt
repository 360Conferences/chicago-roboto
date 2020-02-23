package com.chicagoroboto.data

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import timber.log.error
import java.util.*
import javax.inject.Inject

class FirebaseSessionDateProvider @Inject constructor(
    dbRoot: DatabaseReference
) : SessionDateProvider {

  private val ref: DatabaseReference = dbRoot.child("config").child("event_dates")

  override fun sessionDates(): Flow<List<String>> = channelFlow {
    val listener = ref.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        if (data.exists()) {
          val dates = data.getValue<List<String>>() ?: emptyList()
          offer(dates)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Timber.error(error.toException()) { "Failed to get event dates from Firebase." }
      }
    })

    awaitClose { ref.removeEventListener(listener) }
  }
}
