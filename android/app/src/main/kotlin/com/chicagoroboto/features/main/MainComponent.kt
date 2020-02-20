package com.chicagoroboto.features.main

import com.chicagoroboto.data.UserProvider
import com.chicagoroboto.features.location.LocationComponent
import com.chicagoroboto.features.sessions.SessionListComponent
import com.chicagoroboto.features.speakerlist.SpeakerListComponent
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {
  val userProvider: UserProvider

  fun sessionListComponent(): SessionListComponent
  fun speakerListComponent(): SpeakerListComponent
  fun locationComponent(): LocationComponent
}
