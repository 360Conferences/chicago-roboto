package com.chicagoroboto.features.speakerdetail

import com.chicagoroboto.injection.AppComponent
import dagger.Component

@SpeakerDetailScope
@Component(dependencies = [AppComponent::class])
interface SpeakerDetailComponent {
  fun inject(speakerDetailActivity: SpeakerDetailActivity)

  @Component.Factory
  interface Factory {
    fun create(appComponent: AppComponent): SpeakerDetailComponent
  }
}
