package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SpeakerListModule {

    @Provides fun speakerListPresenter(speakerProvider: SpeakerProvider): SpeakerListMvp.Presenter {
        return SpeakerListPresenter(speakerProvider)
    }

}