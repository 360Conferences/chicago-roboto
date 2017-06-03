package com.conferences.features.main

import com.conferences.features.location.LocationComponent
import com.conferences.features.sessions.SessionListComponent
import com.conferences.features.speakerlist.SpeakerListComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun sessionListComponent(): SessionListComponent
    fun speakerListComponent(): SpeakerListComponent
    fun locationComponent(): LocationComponent
}