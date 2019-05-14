package com.chicagoroboto.model

import com.chicagoroboto.data.DataSnapshotWrapper

class Session(
    val id: String? = null,
    val type: String? = null,
    val title: String? = null,
    val description: String? = null,
    val start_time: String? = null,
    val end_time: String? = null,
    val date: String? = null,
    val speakers: List<String>? = null,
    val location: String? = "Main",
    val address: String? = null,
    val tracks: List<String>? = null
) {

  companion object Factory {
    fun fromDataSnapshot(snapshot: DataSnapshotWrapper): Session? = if (snapshot.exists()) {
      Session(
          id = snapshot["id"] as? String,
          type = snapshot["type"] as? String,
          title = snapshot["title"] as? String,
          description = snapshot["description"] as? String,
          start_time = snapshot["start_time"] as? String,
          end_time = snapshot["end_time"] as? String,
          date = snapshot["date"] as? String,
          speakers = snapshot
              .child("speakers")
              .getList { it.getValue() as String? }
              .filterNotNull(),
          location = snapshot["location"] as? String,
          address = snapshot["address"] as? String,
          tracks = snapshot.child("tracks").getList { it.getValue() as String? }.filterNotNull()
      )
    } else {
      null
    }
  }
}
