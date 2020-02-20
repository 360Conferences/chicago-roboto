package com.chicagoroboto.data

import com.chicagoroboto.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserProvider @Inject constructor(
    private val auth: FirebaseAuth
) : UserProvider {

  private fun FirebaseUser.toUser(): User {
    return User(this.uid)
  }

  override val currentUser: User?
    get() = auth.currentUser?.toUser()

  override fun signIn(onComplete: (user: User?) -> Unit) {
    auth.signInAnonymously().addOnCompleteListener {
      onComplete(auth.currentUser?.toUser())
    }
  }

  override suspend fun signIn(): User? {
    val result = auth.signInAnonymously().await()
    return result.user?.toUser()
  }
}
