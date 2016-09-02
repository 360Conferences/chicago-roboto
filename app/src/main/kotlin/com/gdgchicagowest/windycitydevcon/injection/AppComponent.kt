package com.gdgchicagowest.windycitydevcon.injection

import com.gdgchicagowest.windycitydevcon.data.DataModule
import com.gdgchicagowest.windycitydevcon.features.main.MainComponent
import com.gdgchicagowest.windycitydevcon.features.main.MainModule
import com.gdgchicagowest.windycitydevcon.features.sessiondetail.SessionDetailComponent
import com.gdgchicagowest.windycitydevcon.features.sessiondetail.SessionDetailModule
import com.gdgchicagowest.windycitydevcon.features.speakerdetail.SpeakerDetailComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class))
interface AppComponent {
    fun sessionDetailComponent(sessionDetailModule: SessionDetailModule): SessionDetailComponent
    fun speakerDetailComponent(): SpeakerDetailComponent
    fun mainComponent(mainModule: MainModule): MainComponent
}

