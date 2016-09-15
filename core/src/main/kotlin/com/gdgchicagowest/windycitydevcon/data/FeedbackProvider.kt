package com.gdgchicagowest.windycitydevcon.data

import com.gdgchicagowest.windycitydevcon.model.Feedback

interface FeedbackProvider {
    fun feedback(sessionId: String, onComplete: (feedback: Feedback?) -> Unit)
    fun submitFeedback(sessionId: String, overall: Float, technical: Float, presentation: Float, onComplete: (feedback: Feedback) -> Unit)
}