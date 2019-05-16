package com.chicagoroboto.model

import com.chicagoroboto.data.DataSnapshotWrapper
import com.chicagoroboto.data.get

class Venue(
    val name: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null,
    val phone: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
  companion object Factory {
    fun fromDataSnapshot(snapshot: DataSnapshotWrapper): Venue? = if (snapshot.exists()) {
      Venue(
          name = snapshot["name"] as? String,
          address = snapshot["address"] as? String,
          city = snapshot["city"] as? String,
          state = snapshot["state"] as? String,
          zip = snapshot["zip"] as? String,
          phone = snapshot["phone"] as? String,
          latitude = snapshot["latitude"] as? Double,
          longitude = snapshot["longitude"] as? Double
      )
    } else {
      null
    }
  }
}