package com.conferences.features.sessiondetail

import com.conferences.features.sessiondetail.feedback.FeedbackComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionDetailModule::class))
interface SessionDetailComponent {
    fun inject(sessionDetailView: SessionDetailView)

    fun feedbackComponent(): FeedbackComponent
}