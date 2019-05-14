package com.chicagoroboto.data

import com.chicagoroboto.model.Feedback
import com.chicagoroboto.model.Venue

interface FeedbackProvider {
    fun feedback(sessionId: String, onComplete: (feedback: Feedback?) -> Unit)
    fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (feedback: Feedback) -> Unit)
    fun submitUser(firstName: String, lastName: String)

    @ExperimentalUnsignedTypes
    class Impl(
        db: DatabaseReferenceWrapper,
        private val preferencesProvider: PreferencesProvider
    ) : FeedbackProvider {

        private val ref: DatabaseReferenceWrapper = db.child("feedback")

        private val handles = mutableMapOf<Any, ULong>()
        private val queries = mutableMapOf<Any, DatabaseReferenceWrapper>()

        private data class FeedbackValueEventListener(
            val key: Any, val onComplete: (Feedback?) -> Unit
        ) : ValueEventListenerWrapper {
            override fun onDataChange(data: DataSnapshotWrapper?) {
                onComplete(data?.map(Feedback.Factory::fromDataSnapshot))
            }

            override fun onCancelled(error: Exception) {
                onComplete(null)
            }

        }

        override fun feedback(sessionId: String, onComplete: (Feedback?) -> Unit) {
            val listener = FeedbackValueEventListener(sessionId, onComplete)
            val query = ref.child("scores").child(sessionId).child(preferencesProvider.getId())
            queries[sessionId] = query
            handles[sessionId] = query.addSingleValueEventListener(listener)
        }

        override fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (Feedback) -> Unit) {
            val feedback = Feedback(overall, technical, presentation)
            ref.child("scores").child(sessionId).child(preferencesProvider.getId()).setValue(feedback)
            onComplete(feedback)
        }

        override fun submitUser(firstName: String, lastName: String) {
            ref.child("users").child(preferencesProvider.getId()).setValue("$firstName $lastName")
        }

        private fun removeFeedbackListener(key: String) {
            val query = queries[key]
            val handle = handles[key]
            if (query != null && handle != null) {
                query.removeEventListener(handle)
            }

            queries.remove(key)
            handles.remove(key)
        }
    }
}