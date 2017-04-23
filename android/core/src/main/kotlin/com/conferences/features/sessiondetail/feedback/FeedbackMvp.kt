package com.conferences.features.sessiondetail.feedback

import com.conferences.features.shared.Mvp
import com.conferences.model.Feedback

interface FeedbackMvp {

    interface View : Mvp.View {
        fun setFeedback(feedback: Feedback)
        fun dismiss()
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setSessionId(sessionId: String)

        fun submit(overall: Float, technical: Float, speaker: Float)
    }
}