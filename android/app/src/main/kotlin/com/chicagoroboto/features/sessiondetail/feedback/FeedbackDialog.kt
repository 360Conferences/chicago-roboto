package com.chicagoroboto.features.sessiondetail.feedback

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.chicagoroboto.R
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.sessiondetail.SessionDetailComponent
import com.chicagoroboto.model.Feedback
import kotlinx.android.synthetic.main.dialog_feedback.view.*
import javax.inject.Inject

class FeedbackDialog(context: Context, val sessionId: String) : Dialog(context, true, null), FeedbackMvp.View {

    @Inject lateinit var presenter: FeedbackMvp.Presenter

    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context.getComponent<SessionDetailComponent>().feedbackComponent().inject(this)
        presenter.setSessionId(sessionId)

        view = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null, false)
        view.submit.setOnClickListener {
            presenter.submit(view.overall.rating, view.technical.rating, view.speaker.rating)
        }

        view.cancel.setOnClickListener {
            cancel()
        }

        setContentView(view)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    override fun setFeedback(feedback: Feedback) {
        view.overall.rating = feedback.overall ?: 0f
        view.technical.rating = feedback.technical ?: 0f
        view.speaker.rating = feedback.speaker ?: 0f
    }
}