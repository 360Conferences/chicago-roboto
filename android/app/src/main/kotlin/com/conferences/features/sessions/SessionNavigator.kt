package com.conferences.features.sessions

import com.conferences.model.Session

interface SessionNavigator {
    fun showSession(session: Session)
}