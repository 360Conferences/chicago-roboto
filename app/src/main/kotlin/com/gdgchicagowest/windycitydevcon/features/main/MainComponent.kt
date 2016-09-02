package com.gdgchicagowest.windycitydevcon.features.main

import com.gdgchicagowest.windycitydevcon.features.sessions.SessionListComponent
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun sessionListComponent(): SessionListComponent
}