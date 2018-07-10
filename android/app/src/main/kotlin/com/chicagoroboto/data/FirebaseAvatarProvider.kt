package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference

class FirebaseAvatarProvider(storage: StorageReference) : AvatarProvider {

  private val ref = storage.child("profiles")

  override fun getAvatarUri(speaker: Speaker, callback: (String) -> Unit) {
    val id = speaker.id ?: return
    ref.child(id).downloadUrl.addOnSuccessListener(OnSuccessListener {
      callback(it.toString())
    })
  }
}