package com.chicagoroboto.data

import com.chicagoroboto.model.Feedback
import com.google.firebase.database.*
import javax.inject.Inject

class FirebaseFeedbackProvider @Inject constructor(
    db: DatabaseReference,
    private val preferencesProvider: PreferencesProvider
) : FeedbackProvider {

  private val ref: DatabaseReference = db.child("feedback")

  private val queries: MutableMap<Any, Query> = mutableMapOf()
  private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()

  override fun feedback(sessionId: String, onComplete: (Feedback?) -> Unit) {
    if (queries[sessionId] != null) {
      removeFeedbackListener(sessionId)
    }

    val listener = object : ValueEventListener {
      override fun onDataChange(data: DataSnapshot) {
        onComplete(data.getValue(Feedback::class.java))
      }

      override fun onCancelled(error: DatabaseError) {
        // oh noes
      }
    }
    listeners[sessionId] = listener

    val query = ref.child("scores").child(sessionId).child(preferencesProvider.uid)
    query.addListenerForSingleValueEvent(listener)
    queries[sessionId] = query
  }

  override fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (Feedback) -> Unit) {
    val feedback = Feedback(overall, technical, presentation)
    ref.child("scores").child(sessionId).child(preferencesProvider.uid).setValue(feedback)
    onComplete(feedback)
  }

  override fun submitUser(firstName: String, lastName: String) {
    ref.child("users").child(preferencesProvider.uid).setValue("$firstName $lastName")
  }

  private fun removeFeedbackListener(key: String) {
    queries.remove(key)?.let { query ->
      listeners.remove(key)?.let { query.removeEventListener(it) }
    }
  }
}
