package com.gdgchicagowest.windycitydevcon.features.main

import com.gdgchicagowest.windycitydevcon.features.location.LocationComponent
import com.gdgchicagowest.windycitydevcon.features.sessions.SessionListComponent
import com.gdgchicagowest.windycitydevcon.features.speakerlist.SpeakerListComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun sessionListComponent(): SessionListComponent
    fun speakerListComponent(): SpeakerListComponent
    fun locationComponent(): LocationComponent
}