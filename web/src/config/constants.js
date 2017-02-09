import firebase from 'firebase'

const config = {
  apiKey: "AIzaSyAxy_w3AbORHk8xUGS_LzBOFs0AgTcF2Ag",
  authDomain: "chicago-roboto.firebaseapp.com",
  databaseURL: "https://chicago-roboto.firebaseio.com",
  storageBucket: "chicago-roboto.appspot.com",
  messagingSenderId: "757090852561"
}
firebase.initializeApp(config)

export const db = firebase.database().ref()
export const auth = firebase.auth()
