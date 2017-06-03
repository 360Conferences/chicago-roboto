package com.conferences.features.sessions

import com.conferences.features.sessions.SessionDateView
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionListModule::class))
interface SessionListComponent {
    fun inject(sessionListView: SessionListView)
    fun inject(sessionDateView: SessionDateView)
}
