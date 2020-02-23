package com.chicagoroboto.features.sessions

import dagger.Subcomponent

@Subcomponent
interface SessionListComponent {
    fun inject(sessionListFragment: SessionListFragment)
    fun inject(sessionDateFragment: SessionDateFragment)
}
