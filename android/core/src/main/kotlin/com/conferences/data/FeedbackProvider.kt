package com.conferences.data

import com.conferences.model.Feedback

interface FeedbackProvider {
    fun feedback(sessionId: String, onComplete: (feedback: Feedback?) -> Unit)
    fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (feedback: Feedback) -> Unit)
}