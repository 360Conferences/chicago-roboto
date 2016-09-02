package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SessionDetailModule::class))
interface SessionDetailComponent {
    fun inject(sessionDetailView: SessionDetailView)
}