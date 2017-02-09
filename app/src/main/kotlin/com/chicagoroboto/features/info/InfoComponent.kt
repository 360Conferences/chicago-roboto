package com.chicagoroboto.features.info

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(InfoModule::class))
interface InfoComponent {
    fun inject(infoView: InfoView)
}