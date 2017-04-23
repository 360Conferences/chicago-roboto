package com.conferences.features.sessions

import com.conferences.features.shared.Mvp

interface SessionDateListMvp {

    interface View : Mvp.View {
        fun showNoSessionDates()
        fun showSessionDates(sessionDates: List<String>)
        fun scrollToCurrentDay()
    }

    interface Presenter : Mvp.Presenter<View> {
    }

}