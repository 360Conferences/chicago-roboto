package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import com.gdgchicagowest.windycitydevcon.features.sessiondetail.feedback.FeedbackComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionDetailModule::class))
interface SessionDetailComponent {
    fun inject(sessionDetailView: SessionDetailView)

    fun feedbackComponent(): FeedbackComponent
}