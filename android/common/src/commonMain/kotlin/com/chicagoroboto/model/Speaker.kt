package com.chicagoroboto.model

import com.chicagoroboto.data.DataSnapshotWrapper
import com.chicagoroboto.data.get

class Speaker(
    val id: String? = null,
    val name: String? = null,
    val title: String? = null,
    val company: String? = null,
    val email: String? = null,
    val twitter: String? = null,
    val github: String? = null,
    val bio: String? = null
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true

    other as Speaker

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }

  companion object Factory {
    fun fromDataSnapshot(snapshot: DataSnapshotWrapper): Speaker? = if (snapshot.exists()) {
      Speaker(
          id = snapshot["id"] as? String,
          name = snapshot["name"] as? String,
          title = snapshot["title"] as? String,
          company = snapshot["company"] as? String,
          email = snapshot["email"] as? String,
          twitter = snapshot["twitter"] as? String,
          github = snapshot["github"] as? String,
          bio = snapshot["bio"] as? String
      )
    } else {
      null
    }
  }
}