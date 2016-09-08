package com.gdgchicagowest.windycitydevcon.features.location

import com.gdgchicagowest.windycitydevcon.data.VenueProvider

class LocationPresenter(val venueProvider: VenueProvider) : LocationMvp.Presenter {

    var view: LocationMvp.View? = null

    override fun onAttach(view: LocationMvp.View) {
        this.view = view

        venueProvider.addVenueListener(this, { venue ->
            view?.showVenue(venue)
        })
    }

    override fun onDetach() {
        view = null
        venueProvider.removeVenueListener(this)
    }
}