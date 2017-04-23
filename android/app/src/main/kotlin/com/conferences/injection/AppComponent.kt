package com.conferences.injection

import com.conferences.data.DataModule
import com.conferences.features.info.InfoComponent
import com.conferences.features.info.InfoModule
import com.conferences.features.main.MainComponent
import com.conferences.features.main.MainModule
import com.conferences.features.sessiondetail.SessionDetailComponent
import com.conferences.features.sessiondetail.SessionDetailModule
import com.conferences.features.speakerdetail.SpeakerDetailComponent
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

