package com.conferences.data

import com.conferences.model.Venue

interface VenueProvider {
    fun addVenueListener(key: Any, onComplete: (venue: Venue?) -> Unit)
    fun removeVenueListener(key: Any)
}