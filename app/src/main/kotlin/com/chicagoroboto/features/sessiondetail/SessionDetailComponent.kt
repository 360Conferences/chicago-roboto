package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.features.sessiondetail.feedback.FeedbackComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionDetailModule::class))
interface SessionDetailComponent {
    fun inject(sessionDetailView: SessionDetailView)

    fun feedbackComponent(): FeedbackComponent
}