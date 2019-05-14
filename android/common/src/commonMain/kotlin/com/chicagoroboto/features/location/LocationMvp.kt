package com.chicagoroboto.features.location

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Venue

interface LocationMvp {

    interface View : Mvp.View {
        fun showVenue(venue: Venue?)
    }

    interface Presenter : Mvp.Presenter<View> {

    }
}