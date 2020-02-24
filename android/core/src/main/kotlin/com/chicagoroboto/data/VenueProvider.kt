package com.chicagoroboto.data

import com.chicagoroboto.model.Venue
import kotlinx.coroutines.flow.Flow

interface VenueProvider {
  fun venue(): Flow<Venue>
}
