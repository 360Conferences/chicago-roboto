package com.chicagoroboto.data

import com.chicagoroboto.model.User

interface UserProvider {
  val currentUser: User?
  fun signIn(onComplete: (user: User?) -> Unit)
  suspend fun signIn(): User?
}
