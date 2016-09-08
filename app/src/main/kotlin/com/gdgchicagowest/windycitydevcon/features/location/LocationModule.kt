package com.gdgchicagowest.windycitydevcon.features.location

import com.gdgchicagowest.windycitydevcon.data.VenueProvider
import dagger.Module
import dagger.Provides

@Module
class LocationModule {
    @Provides fun locationPresenter(venueProvider: VenueProvider): LocationMvp.Presenter {
        return LocationPresenter(venueProvider)
    }
}