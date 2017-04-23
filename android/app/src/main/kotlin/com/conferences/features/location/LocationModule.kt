package com.conferences.features.location

import com.conferences.data.VenueProvider
import dagger.Module
import dagger.Provides

@Module
class LocationModule {
    @Provides fun locationPresenter(venueProvider: VenueProvider): LocationMvp.Presenter {
        return LocationPresenter(venueProvider)
    }
}