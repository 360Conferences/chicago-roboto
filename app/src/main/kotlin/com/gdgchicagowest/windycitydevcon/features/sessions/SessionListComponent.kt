package com.gdgchicagowest.windycitydevcon.features.sessions

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionListModule::class))
interface SessionListComponent {
    fun inject(sessionListView: SessionListView)
}
