package com.chicagoroboto.features.location

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(LocationModule::class))
interface LocationComponent {
    fun inject(locationView: LocationView)
}