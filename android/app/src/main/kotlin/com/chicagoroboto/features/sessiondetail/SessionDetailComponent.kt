package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.features.sessiondetail.feedback.FeedbackComponent
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface SessionDetailComponent {
  fun inject(sessionDetailActivity: SessionDetailActivity)

  fun feedbackComponent(): FeedbackComponent

  @Subcomponent.Factory
  interface Factory {
    fun create(
        @BindsInstance speakerNavigator: SpeakerNavigator
    ): SessionDetailComponent
  }
}
