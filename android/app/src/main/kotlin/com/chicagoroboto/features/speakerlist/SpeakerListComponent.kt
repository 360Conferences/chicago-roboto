package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.features.sessiondetail.SessionDetailComponent
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface SpeakerListComponent {
  fun inject(speakerListView: SpeakerListView)

  @Subcomponent.Factory
  interface Factory {
    fun create(): SpeakerListComponent
  }
}
