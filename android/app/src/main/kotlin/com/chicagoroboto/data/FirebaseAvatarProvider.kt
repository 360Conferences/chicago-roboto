package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseAvatarProvider(storage: StorageReference) : AvatarProvider {

  private val ref = storage.child("profiles")

  override suspend fun getAvatarUri(speaker: Speaker) = suspendCoroutine<String> { c ->
    val id = speaker.id ?: ""
    if (id.isEmpty()) {
      c.resume("")
    }
    else {
      ref.child(id).downloadUrl.addOnSuccessListener({
        c.resume(it.toString())
      })
    }
  }
}