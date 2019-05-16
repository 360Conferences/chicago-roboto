package com.chicagoroboto.model

import com.chicagoroboto.data.DataSnapshotWrapper
import com.chicagoroboto.data.get

class Feedback(
    val overall: Float? = null,
    val technical: Float? = null,
    val speaker: Float? = null
) {
  companion object Factory {
    fun fromDataSnapshot(snapshot: DataSnapshotWrapper): Feedback? = if (snapshot.exists()) {
      Feedback(
          overall = snapshot["overall"] as? Float,
          technical = snapshot["technical"] as? Float,
          speaker = snapshot["speaker"] as? Float
      )
    } else {
      null
    }
  }
}