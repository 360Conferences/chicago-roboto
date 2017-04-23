package com.conferences.features.sessiondetail.feedback

import com.conferences.data.FeedbackProvider
import dagger.Module
import dagger.Provides

@Module
class FeedbackModule {
    @Provides fun presenter(feedbackProvider: FeedbackProvider): FeedbackMvp.Presenter {
        return FeedbackPresenter(feedbackProvider)
    }
}