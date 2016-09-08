package com.gdgchicagowest.windycitydevcon.data

import com.gdgchicagowest.windycitydevcon.model.Venue

interface VenueProvider {
    fun addVenueListener(key: Any, onComplete: (venue: Venue?) -> Unit)
    fun removeVenueListener(key: Any)
}