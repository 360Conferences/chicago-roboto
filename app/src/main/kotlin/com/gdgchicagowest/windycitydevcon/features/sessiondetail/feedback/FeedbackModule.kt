package com.gdgchicagowest.windycitydevcon.features.sessiondetail.feedback

import com.gdgchicagowest.windycitydevcon.data.FeedbackProvider
import dagger.Module
import dagger.Provides

@Module
class FeedbackModule {
    @Provides fun presenter(feedbackProvider: FeedbackProvider): FeedbackMvp.Presenter {
        return FeedbackPresenter(feedbackProvider)
    }
}