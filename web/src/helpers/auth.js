import { auth, db } from '../config/constants'
import * as firebase from 'firebase'

export function login() {
  var provider = new firebase.auth.GoogleAuthProvider()
  return auth.signInWithPopup(provider)
}

export function logout() {
  return auth.signOut()
}

export function loggedIn() {
  return auth.currentUser
}

export function isAdmin() {
  return db.child('admins').once('value')
      .then((snapshot) => {
        return auth.currentUser && snapshot.val().contains(auth.currentUser.email)
      })
}
