package com.chicagoroboto.features.speakerdetail

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(SpeakerDetailModule::class))
interface SpeakerDetailComponent {
    fun inject(speakerDetailView: SpeakerDetailView)
}