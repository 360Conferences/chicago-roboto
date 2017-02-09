package com.chicagoroboto.features.main

import com.chicagoroboto.features.location.LocationComponent
import com.chicagoroboto.features.sessions.SessionListComponent
import com.chicagoroboto.features.speakerlist.SpeakerListComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun sessionListComponent(): SessionListComponent
    fun speakerListComponent(): SpeakerListComponent
    fun locationComponent(): LocationComponent
}