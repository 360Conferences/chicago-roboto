package com.chicagoroboto.injection

import com.chicagoroboto.data.DataModule
import com.chicagoroboto.features.info.InfoComponent
import com.chicagoroboto.features.info.InfoModule
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.main.MainModule
import com.chicagoroboto.features.sessiondetail.SessionDetailComponent
import com.chicagoroboto.features.sessiondetail.SessionDetailModule
import com.chicagoroboto.features.speakerdetail.SpeakerDetailComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class))
interface AppComponent {
    fun sessionDetailComponent(sessionDetailModule: SessionDetailModule): SessionDetailComponent
    fun speakerDetailComponent(): SpeakerDetailComponent
    fun mainComponent(mainModule: MainModule): MainComponent
    fun infoComponent(infoModule: InfoModule): InfoComponent
}

