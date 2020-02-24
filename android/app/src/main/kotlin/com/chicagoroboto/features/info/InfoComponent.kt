package com.chicagoroboto.features.info

import dagger.Subcomponent

@Subcomponent(modules = [InfoModule::class])
interface InfoComponent {
    fun inject(infoFragment: InfoFragment)
}
