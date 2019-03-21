package com.chicagoroboto.data

import com.chicagoroboto.model.Feedback

interface FeedbackProvider {
    fun feedback(sessionId: String, onComplete: (feedback: Feedback?) -> Unit)
    fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (feedback: Feedback) -> Unit)
    fun submitUser(firstName: String, lastName: String)
}