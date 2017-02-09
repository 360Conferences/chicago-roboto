package com.chicagoroboto.features.speakerdetail

import com.chicagoroboto.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SpeakerDetailModule {
    @Provides fun speakerDetailPresenter(speakerProvider: SpeakerProvider): SpeakerDetailMvp.Presenter {
        return SpeakerDetailPresenter(speakerProvider)
    }
}