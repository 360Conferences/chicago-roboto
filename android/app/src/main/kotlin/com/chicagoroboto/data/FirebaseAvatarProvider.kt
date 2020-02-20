package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class FirebaseAvatarProvider @Inject constructor(
    storage: StorageReference
) : AvatarProvider {

  private val ref = storage.child("profiles")

  override fun getAvatarUri(speaker: Speaker, callback: (String) -> Unit) {
    val id = speaker.id ?: return
    ref.child(id).downloadUrl.addOnSuccessListener {
      callback(it.toString())
    }
  }
}
