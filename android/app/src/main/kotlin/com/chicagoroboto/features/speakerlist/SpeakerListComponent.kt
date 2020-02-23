package com.chicagoroboto.features.speakerlist

import dagger.Subcomponent

@Subcomponent
interface SpeakerListComponent {
  fun inject(speakerListFragment: SpeakerListFragment)

  @Subcomponent.Factory
  interface Factory {
    fun create(): SpeakerListComponent
  }
}
