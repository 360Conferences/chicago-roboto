package com.chicagoroboto.features.location

import com.chicagoroboto.data.VenueProvider
import dagger.Module
import dagger.Provides

@Module
class LocationModule {
    @Provides fun locationPresenter(venueProvider: VenueProvider): LocationMvp.Presenter {
        return LocationPresenter(venueProvider)
    }
}