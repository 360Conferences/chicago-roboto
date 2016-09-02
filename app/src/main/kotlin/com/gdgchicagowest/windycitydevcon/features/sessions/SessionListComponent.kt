package com.gdgchicagowest.windycitydevcon.features.sessions

import com.gdgchicagowest.windycitydevcon.features.sessions.SessionDateView
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionListModule::class))
interface SessionListComponent {
    fun inject(sessionListView: SessionListView)
    fun inject(sessionDateView: SessionDateView)
}
