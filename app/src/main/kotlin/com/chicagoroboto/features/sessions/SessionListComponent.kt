package com.chicagoroboto.features.sessions

import com.chicagoroboto.features.sessions.SessionDateView
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionListModule::class))
interface SessionListComponent {
    fun inject(sessionListView: SessionListView)
    fun inject(sessionDateView: SessionDateView)
}
