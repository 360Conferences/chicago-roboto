package com.chicagoroboto.features.sessiondetail.feedback

import com.chicagoroboto.data.FeedbackProvider
import dagger.Module
import dagger.Provides

@Module
class FeedbackModule {
    @Provides fun presenter(feedbackProvider: FeedbackProvider): FeedbackMvp.Presenter {
        return FeedbackPresenter(feedbackProvider)
    }
}