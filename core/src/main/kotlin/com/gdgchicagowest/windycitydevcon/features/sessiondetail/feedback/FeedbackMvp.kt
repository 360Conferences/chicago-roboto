package com.gdgchicagowest.windycitydevcon.features.sessiondetail.feedback

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Feedback

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