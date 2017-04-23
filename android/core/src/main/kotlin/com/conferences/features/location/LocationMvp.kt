package com.conferences.features.location

import com.conferences.features.shared.Mvp
import com.conferences.model.Venue

interface LocationMvp {

    interface View : Mvp.View {
        fun showVenue(venue: Venue?)
    }

    interface Presenter : Mvp.Presenter<View> {

    }
}