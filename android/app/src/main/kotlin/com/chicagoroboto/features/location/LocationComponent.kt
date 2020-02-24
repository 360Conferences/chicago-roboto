package com.chicagoroboto.features.location

import dagger.Subcomponent

@Subcomponent
interface LocationComponent {
    fun inject(locationFragment: LocationFragment)
}
