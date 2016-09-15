package com.gdgchicagowest.windycitydevcon.features.sessiondetail.feedback

import com.gdgchicagowest.windycitydevcon.data.FeedbackProvider

class FeedbackPresenter(private val feedbackProvider: FeedbackProvider) : FeedbackMvp.Presenter {

    private var view: FeedbackMvp.View? = null
    private var sessionId: String? = null

    override fun onAttach(view: FeedbackMvp.View) {
        this.view = view

        val sessionId = this.sessionId
        if (sessionId != null) {
            feedbackProvider.feedback(sessionId, { feedback ->
                if (feedback != null) {
                    this.view?.setFeedback(feedback)
                }
            })
        }
    }

    override fun onDetach() {
        this.view = null
    }

    override fun setSessionId(sessionId: String) {
        this.sessionId = sessionId
    }

    override fun submit(overall: Float, technical: Float, speaker: Float) {
        val sessionId = this.sessionId
        if (sessionId != null) {
            feedbackProvider.submitFeedback(sessionId, overall, technical, speaker, {
                view?.dismiss()
            })
        }
    }
}