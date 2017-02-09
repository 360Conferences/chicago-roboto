package com.chicagoroboto.data

import com.chicagoroboto.model.Venue

interface VenueProvider {
    fun addVenueListener(key: Any, onComplete: (venue: Venue?) -> Unit)
    fun removeVenueListener(key: Any)
}