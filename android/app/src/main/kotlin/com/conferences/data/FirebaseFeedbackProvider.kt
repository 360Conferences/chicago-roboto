package com.conferences.data

import com.conferences.model.Feedback
import com.google.firebase.database.*

class FirebaseFeedbackProvider(private val db: DatabaseReference,
                               private val preferencesProvider: PreferencesProvider) : FeedbackProvider {

    private val ref: DatabaseReference = db.child("feedback")

    private val queries: MutableMap<Any, Query> = mutableMapOf()
    private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()

    override fun feedback(sessionId: String, onComplete: (Feedback?) -> Unit) {
        if (queries[sessionId] != null) {
            removeFeedbackListener(sessionId)
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                onComplete(data?.getValue(Feedback::class.java))
            }

            override fun onCancelled(error: DatabaseError?) {
                // oh noes
            }
        }
        listeners[sessionId] = listener

        val query = ref.child(sessionId).child(preferencesProvider.getId())
        query.addListenerForSingleValueEvent(listener)
        queries[sessionId] = query
    }

    override fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (Feedback) -> Unit) {
        val feedback = Feedback(overall, technical, presentation)
        ref.child(sessionId).child(preferencesProvider.getId()).setValue(feedback)
        onComplete(feedback)
    }

    private fun removeFeedbackListener(key: String) {
        val query = queries[key]
        query?.removeEventListener(listeners[key])

        queries.remove(key)
        listeners.remove(key)
    }
}