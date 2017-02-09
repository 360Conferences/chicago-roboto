package com.chicagoroboto.features.sessiondetail.feedback

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Feedback

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