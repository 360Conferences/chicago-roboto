package com.gdgchicagowest.windycitydevcon.features.dates

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionDateModule::class))
interface SessionDateComponent {
    fun inject(sessionDateView: SessionDateView)
}