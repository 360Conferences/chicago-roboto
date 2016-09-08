package com.gdgchicagowest.windycitydevcon.features.location

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Venue

interface LocationMvp {

    interface View : Mvp.View {
        fun showVenue(venue: Venue?)
    }

    interface Presenter : Mvp.Presenter<View> {

    }
}