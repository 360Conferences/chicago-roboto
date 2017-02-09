package com.chicagoroboto.features.sessions

import com.chicagoroboto.features.shared.Mvp

interface SessionDateListMvp {

    interface View : Mvp.View {
        fun showNoSessionDates()
        fun showSessionDates(sessionDates: List<String>)
    }

    interface Presenter : Mvp.Presenter<View> {
    }

}